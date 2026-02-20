package fr.forty_two.orm.sql;

import java.lang.reflect.Field;

public record Column(String name, String sqlType, int length, Field field) {
    public Column {
        field.setAccessible(true);
    }
}
