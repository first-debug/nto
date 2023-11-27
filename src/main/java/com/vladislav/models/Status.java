package com.vladislav.models;

public enum Status {
    CREATED("Создана"),
    EXECUTED("К выполнению"),
    COMPLETED("Выполнена"),
    OVERDUE("Просрочена");

    private final String name;


    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
