package com.vladislav.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class EventType {
    public final static ArrayList<EventType> objectsList = new ArrayList<>();

    private final IntegerProperty id;
    private final StringProperty name;
    private final boolean isEntertainment;
    private final StringProperty isEntertainmentString;

    public EventType(Integer id, String name, boolean isEntertainment)
    {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.isEntertainment = isEntertainment;
        this.isEntertainmentString = new SimpleStringProperty(this.isEntertainment ? "Да" : "Нет");
        EventType.objectsList.add(this);
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