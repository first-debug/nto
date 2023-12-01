package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public enum Status {
    CREATED("Создана"),
    EXECUTED("К выполнению"),
    COMPLETED("Выполнена"),
    OVERDUE("Просрочена");

    private final StringProperty name;


    Status(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }
}
