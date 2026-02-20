package fr.forty_two.orm.sql;

import java.lang.reflect.Field;
import java.util.Map;

import fr.forty_two.orm.annotations.OrmColumn;
public class SqlTypeResolver {
    private static final Map<String, String> TYPE_MAP = Map.of(
        "java.lang.Long", "BIGINT",
        "java.lang.Integer", "INTEGER",
        "java.lang.Boolean", "BOOLEAN",
        "java.lang.Double", "DOUBLE PRECISION",
        "java.lang.String", "VARCHAR"
    );

    private SqlTypeResolver() {}

    public static String resolve(Field field) {
        String fieldType = field.getType().getName();

        if (fieldType.equals("java.lang.String")) {
            OrmColumn ormCol = field.getAnnotation(OrmColumn.class);
            if (ormCol != null) {
                int length = ormCol.length();
                return length > 0 ?"VARCHAR(" + length + ")" : "TEXT";
            }
            return "TEXT";
        }

        String sqlType = TYPE_MAP.get(fieldType);

        if (sqlType == null) {
            throw new IllegalStateException("Unsupported field type '" + fieldType + "' on field '" + field.getName() + "'");
        }

        return sqlType;
    }

    public static boolean isSupported(Field field) {
        return TYPE_MAP.containsKey(field.getType().getName());
    }
}
