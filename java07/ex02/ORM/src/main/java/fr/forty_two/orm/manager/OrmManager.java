package fr.forty_two.orm.manager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.sql.DataSource;

import fr.forty_two.orm.annotations.OrmColumn;
import fr.forty_two.orm.annotations.OrmColumnId;
import fr.forty_two.orm.annotations.OrmEntity;
import fr.forty_two.orm.sql.Column;
import fr.forty_two.orm.sql.SqlTypeResolver;
import fr.forty_two.orm.sql.Table;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class OrmManager {
    private final DataSource engine;
    private final Map<Class<?>, Table> tablesMap = new HashMap<>();

    public OrmManager(DataSource ds) {
        this.engine = ds;
    }

    public void run(String pckg) {
        try (ScanResult result = new ClassGraph()
            .enableAllInfo()
            .acceptPackages(pckg)
            .scan();
            ) {

            for (ClassInfo clazz : result.getClassesWithAnnotation(OrmEntity.class)) {
                Class<?> entityClass = clazz.loadClass(); // we can load class after determining it is annotated with @OrmEntity
                AnnotationInfo ormEntity = clazz.getAnnotationInfo(OrmEntity.class);
                String ormEntityTableValue = (String) ormEntity.getParameterValues().getValue("table");
                String tableName
                    = ormEntityTableValue.equals("__UNSET__") || ormEntityTableValue.isBlank()
                        ? clazz.getSimpleName()
                        : ormEntityTableValue;

                Column idColumn = null;
                List<Column> colsMetaData = new ArrayList<>();
                StringJoiner j = new StringJoiner(",", "(", ")");
                j.add("id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY");

                for (Field field : entityClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(OrmColumnId.class)) {
                        if (!field.getType().getName().equals("java.lang.Long")) {
                            throw new IllegalStateException("@OrmColumnId on field '" + field.getName() + "' requires a Long");
                        }

                        if (idColumn != null) {
                            throw new IllegalStateException("Duplicate @OrmColumnId in '" + clazz.getName() + "' class");
                        }

                        idColumn = new Column("id", "BIGINT", -1, field);

                    } else if (field.isAnnotationPresent(OrmColumn.class)) {
                        OrmColumn annotation = field.getAnnotation(OrmColumn.class);
                        String sqlType = SqlTypeResolver.resolve(field);
                        String colName = annotation.name();
                        int length = annotation.length();

                        colsMetaData.add(new Column(
                            colName,
                            sqlType,
                            length,
                            field
                        ));
                        
                        j.add(colName + " " + sqlType);
                    }
                }
                
                if (idColumn == null) {
                    throw new IllegalStateException("missing @OrmColumnId on '" + clazz.getSimpleName() + "' class");
                }

                this.tablesMap.put(entityClass, new Table(tableName, idColumn, colsMetaData));

                String ddlSql = "CREATE TABLE " + sanitizeIdentifier(tableName) + j.toString();
                String dropSql = "DROP TABLE IF EXISTS " + sanitizeIdentifier(tableName);

                try (Connection conn = this.engine.getConnection();
                    Statement stmt = conn.createStatement()){
                    stmt.execute(dropSql);
                    System.out.println(dropSql);
                    stmt.execute(ddlSql);
                    System.out.println(ddlSql);
                } catch (SQLException e) {
                    System.err.println("could not create table '" + tableName + "' : " + e);
                }
            }
        }
    }

    public <T> T findById(Long id, Class<T> aClass) {
        if (id == null || aClass == null || !tablesMap.containsKey(aClass)) {
            throw new IllegalArgumentException();
        }
        
        Table table = tablesMap.get(aClass);
        String sql = "SELECT * FROM " + sanitizeIdentifier(table.name()) + " WHERE id = ?";

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    T instance = aClass.getDeclaredConstructor().newInstance();
                    for (Column col : table.columns()) {
                        col.field().set(instance, rs.getObject(col.name()));
                    }
                    table.idColumn().field().set(instance, rs.getLong("id"));
                    System.out.println(sql);
                    return instance;
                }
            } 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void update(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!tablesMap.containsKey(entityClass)) {
            throw new IllegalArgumentException("Unknown entity '" + entityClass.getName() + "'");
        }
        Table table = tablesMap.get(entityClass);
        StringJoiner placeholders = new StringJoiner(",", " " , " ");
        StringBuilder sql = new StringBuilder("UPDATE ");
        List<Object> values = new ArrayList<>();
        for (Column col : table.columns()) {
            Field field = col.field();
            try {
                Object value = field.get(entity);
                placeholders.add("%s = ?".formatted(sanitizeIdentifier(col.name())));
                values.add(value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Long id = (Long)table.idColumn().field().get(entity);
            sql.append(sanitizeIdentifier(table.name()))
                .append(" SET")
                .append(placeholders)
                .append("WHERE id = " + id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        System.out.println(sql);
        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            ps.executeUpdate();
            System.out.println(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!tablesMap.containsKey(entityClass)) {
            throw new IllegalArgumentException("Unknown entity '" + entityClass.getName() + "'");
        }

        Table table = tablesMap.get(entityClass);
        StringJoiner colsNames = new StringJoiner(",", " (" , ")");
        StringJoiner placeholders = new StringJoiner(",", "(" , ")");
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        List<Object> values = new ArrayList<>();
        for (Column col : table.columns()) {
            Field field = col.field();
            colsNames.add(sanitizeIdentifier(col.name()));
            placeholders.add("?");
            try {
                values.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sql.append(sanitizeIdentifier(table.name()))
            .append(colsNames)
            .append(" VALUES")
            .append(placeholders);

        try (Connection conn = engine.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }

            ps.executeUpdate();
            System.out.println(sql);
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Field idField = table.idColumn().field();
                    idField.set(entity, rs.getLong(1));
                }
            } 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private String sanitizeIdentifier(String name) {
        return "\"" +  name.toLowerCase() + "\"";
    }
}
