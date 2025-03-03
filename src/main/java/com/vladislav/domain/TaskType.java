package com.vladislav.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TaskType {
    public final static ObservableList<TaskType> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private final StringProperty name;
    private final StringProperty description;

    private TaskType(Integer id, String name, String description) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.id = id;
        objectsList.add(this);
        objectsId.add(id);
    }

    public static TaskType getInstance(Integer id, String name, String description) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new TaskType(id, name, description);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setName(name);
                    f.setDescription(description);
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(TaskType taskType) {
        objectsList.remove(taskType);
        objectsId.remove(taskType.id);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
