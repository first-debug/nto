package com.vladislav.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Space {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final IntegerProperty area;
    private final IntegerProperty capacity;

    public Space(Integer id, String name, String description, Integer area, Integer capacity) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.area = new SimpleIntegerProperty(area);
        this.capacity = new SimpleIntegerProperty(capacity);
    }

    public String getName() {
        return name.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Integer getArea() {
        return area.get();
    }

    public Integer getCapacity() {
        return capacity.get();
    }

    public Integer getId() {
        return id.get();
    }
}
