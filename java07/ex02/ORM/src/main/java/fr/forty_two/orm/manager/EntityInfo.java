package fr.forty_two.orm.manager;

import java.util.ArrayList;
import java.util.List;

public class EntityInfo {
    private Number id;
    private List<Object> columns = new ArrayList<>();

    public EntityInfo() {

    }

    public Number getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean addColumn(Object column) {
        return this.columns.add(column);
    }
}
