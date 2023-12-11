package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class CoterieType {
    public static final ObservableList<CoterieType> objectsList = FXCollections.observableArrayList();
    public static final ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private final StringProperty name;
    private final StringProperty description;

    private CoterieType(Integer id, String name, String description) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        objectsList.add(this);
        objectsId.add(id);
    }

    public static CoterieType getInstance(Integer id, String name, String description) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new CoterieType(id, name, description);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setName(name);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(CoterieType type) {
        objectsList.remove(type);
        objectsId.remove(type.id);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
