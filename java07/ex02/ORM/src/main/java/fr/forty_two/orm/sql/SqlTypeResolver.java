package fr.forty_two.orm.sql;

import java.util.Map;

import fr.forty_two.orm.annotations.OrmColumn;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.BaseTypeSignature;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.TypeSignature;

public class SqlTypeResolver {
    private static final Map<String, String> TYPE_MAP = Map.of(
        "java.lang.Long", "BIGINT",
        "java.lang.Integer", "INTEGER",
        "java.lang.Boolean", "BOOLEAN",
        "java.lang.Double", "DOUBLE PRECISION",
        "java.lang.String", "VARCHAR"
    );

    private SqlTypeResolver() {}

    public static String resolve(FieldInfo field) {
        String fieldType = extractTypeName(field);

        if (fieldType.equals("java.lang.String")) {
            AnnotationInfo ormCol = field.getAnnotationInfo(OrmColumn.class);
            if (ormCol != null) {
                int length = (int) ormCol.getParameterValues().getValue("length");
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

    public static boolean isSupported(FieldInfo field) {
        return TYPE_MAP.containsKey(extractTypeName(field));
    }

    public static String extractTypeName(FieldInfo field) {
        TypeSignature typeSig = field.getTypeDescriptor();

        if (typeSig instanceof BaseTypeSignature primitiveType) {
            return primitiveType.getTypeStr();
        }
        
        if (typeSig instanceof ClassRefTypeSignature classType) {
            return classType.getFullyQualifiedClassName();
        }

        return "null";
    }
}
