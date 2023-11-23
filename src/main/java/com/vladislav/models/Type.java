package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Type
{
    private final StringProperty name;
    private final StringProperty isEntertainment;

    public Type(String name, String isEntertainment)
    {
        this.name = new SimpleStringProperty(name);
        this.isEntertainment = new SimpleStringProperty(isEntertainment);
    }

    public String getName() {
        return name.getValue();
    }

    public String getIsEntertainment() {
        return isEntertainment.getValue();
    }

    public void setName(StringProperty name) {
        this.name.set(name.get());
    }

    public void setIsEntertainment(String isEntertainment) {
        this.isEntertainment.set(isEntertainment);
    }
}