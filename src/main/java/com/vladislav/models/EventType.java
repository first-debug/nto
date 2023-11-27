package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventType
{
    private final StringProperty name;
    private final boolean isEntertainment;
    private final StringProperty isEntertainmentString;

    public EventType(String name, boolean isEntertainment)
    {
        this.name = new SimpleStringProperty(name);
        this.isEntertainment = isEntertainment;
        this.isEntertainmentString = new SimpleStringProperty(this.isEntertainment ? "Да" : "Нет");
    }

    public String getName() {
        return name.getValue();
    }

    public boolean getIsEntertainment() {
        return isEntertainment;
    }

    public String getIsEntertainmentString() {
        return isEntertainmentString.get();
    }
}