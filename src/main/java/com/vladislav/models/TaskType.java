package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class TaskType {
    public final static ArrayList<TaskType> objectsList = new ArrayList<>();

    private final Integer id;
    private final StringProperty name;
    private final StringProperty description;

    public TaskType(Integer id, String name, String description) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.id = id;
        TaskType.objectsList.add(this);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
