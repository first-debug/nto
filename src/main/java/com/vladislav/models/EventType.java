package com.vladislav.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EventType {
    public final static ObservableList<EventType> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final IntegerProperty id;
    private final StringProperty name;
    private final boolean isEntertainment;
    private final StringProperty isEntertainmentString;

    private EventType(Integer id, String name, boolean isEntertainment)
    {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.isEntertainment = isEntertainment;
        this.isEntertainmentString = new SimpleStringProperty(this.isEntertainment ? "Да" : "Нет");
        objectsList.add(this);
        objectsId.add(id);
    }

    public static EventType getInstance(Integer id, String name, boolean isEntertainment) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new EventType(id, name, isEntertainment);
        } else {
            objectsList.forEach(f -> {
                if (f.getId() == id) {
                    f.setName(name);
                    f.setIsEntertainmentString(isEntertainment);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(EventType eventType) {
        objectsList.remove(eventType);
        objectsId.remove(eventType.getId());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public boolean getIsEntertainment() {
        return isEntertainment;
    }

    public String getIsEntertainmentString() {
        return isEntertainmentString.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setIsEntertainmentString(boolean isEntertainment) {
        this.isEntertainmentString.set(isEntertainment ? "Да" : "Нет");
    }
}