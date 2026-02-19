package fr.forty_two.orm.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.sql.DataSource;

import fr.forty_two.orm.annotations.OrmColumn;
import fr.forty_two.orm.annotations.OrmColumnId;
import fr.forty_two.orm.annotations.OrmEntity;
import fr.forty_two.orm.sql.SqlTypeResolver;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.ScanResult;

public class OrmManager {
    private final DataSource engine;
    
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
                AnnotationInfo ormEntity = clazz.getAnnotationInfo(OrmEntity.class);
                String ormEntityTableValue = (String) ormEntity.getParameterValues().getValue("table");
                String tableName
                    = ormEntityTableValue.equals("__UNSET__") || ormEntityTableValue.isBlank()
                        ? clazz.getSimpleName()
                        : ormEntityTableValue;

                List<FieldInfo> columns = new ArrayList<>();
                boolean hasId = false;
                for (FieldInfo field: clazz.getDeclaredFieldInfo()) {
                    if (field.hasAnnotation(OrmColumnId.class)) {
                        if (!field.getTypeDescriptor().toStringWithSimpleNames().equals("Long")) {
                            throw new IllegalStateException("@OrmColumnId on field '" + field.getName() + "' requires a Long");
                        }

                        if (hasId) {
                            throw new IllegalStateException("Duplicate @OrmColumnId in '" + clazz.getSimpleName() + "' class");
                        }
                        hasId = true;
                        continue;
                    }
                    
                    if (field.hasAnnotation(OrmColumn.class)) {
                        columns.add(field);
                    }
                }
                if (!hasId) {
                    throw new IllegalStateException("missing @OrmColumnId on '" + clazz.getSimpleName() + "' class");
                }
                String ddlSql = buildTableSchema(tableName, columns);
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

    private String buildTableSchema(String tableName, List<FieldInfo> columns) {
        StringJoiner joiner  = new StringJoiner(",", "(", ")");
        joiner.add("id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY");

        for (FieldInfo column : columns) {
            AnnotationInfo columnAnnotation = column.getAnnotationInfo(OrmColumn.class);
            AnnotationParameterValueList params = columnAnnotation.getParameterValues();

            String sqlType = SqlTypeResolver.resolve(column);
            String colName = (String) params.getValue("name");
            int length = (int) params.getValue("length");


            if (sqlType.equals("VARCHAR") && length > 0) {
                sqlType += "(%d)".formatted(length);
            }

            joiner.add(colName + " " + sqlType);
        }
        return "CREATE TABLE " + sanitizeIdentifier(tableName) + joiner.toString();
    }


    // public <T> T findById(Long id, Class<T> aClass) {

    // }


    private String sanitizeIdentifier(String name) {
        return "\"" +  name.toLowerCase() + "\"";
    }
}
