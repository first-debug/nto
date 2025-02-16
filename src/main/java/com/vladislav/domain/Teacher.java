package com.vladislav.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Teacher {
    public static final ObservableList<Teacher> objectsList = FXCollections.observableArrayList();
    public static final ArrayList<Integer> objectsId = new ArrayList<>();
    private final Integer id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty patronymic;

    private Teacher(Integer id, String firstName, String lastName, String patronymic) {
        this.id = id;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.patronymic = new SimpleStringProperty(patronymic);
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Teacher getInstance(Integer id, String firstName, String lastName,
                                      String patronymic) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Teacher(id, firstName, lastName, patronymic);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setFirstName(firstName);
                    f.setLastName(lastName);
                    f.setPatronymic(patronymic);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Teacher teacher) {
        objectsList.remove(teacher);
        objectsId.remove(teacher.getId());
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic.get();
    }

    public void setPatronymic(String patronymic) {
        this.patronymic.set(patronymic);
    }

    public StringProperty patronymicProperty() {
        return patronymic;
    }

    public String getAbbreviatedName() {
        return lastName.get() + " " +
                firstName.get().charAt(0) + ". " +
                patronymic.get().charAt(0) + ".";
    }
}
