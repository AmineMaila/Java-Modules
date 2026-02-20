package fr.forty_two.orm.sql;

import java.util.List;

public record Table(String name, Column idColumn, List<Column> columns) {
}
