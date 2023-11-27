package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskType {

    private final Integer id;
    private final StringProperty name;
    private final StringProperty description;

    public TaskType(Integer id, String name, String description)
    {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.id = id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setName(StringProperty name) {
        this.name.set(name.get());
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Integer getId() {
        return id;
    }
}
