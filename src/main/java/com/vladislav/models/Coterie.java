package com.vladislav.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.dateFormat;

public class Coterie {
    public static ObservableList<Coterie> objectsList = FXCollections.observableArrayList();
    public static ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private final StringProperty title;
    private Long timeStart;
    private final StringProperty stringTime;
    private final Property<CoterieType> type;
    private final Property<Space> space;
    private final Property<Teacher> teacher;
    private final StringProperty teacherFirstName;
    private final StringProperty teacherLastName;
    private final StringProperty teacherPatronymic;
    private Long[][] schedule;

    private Coterie(Integer id, String title, Long timeStart, CoterieType type, Space eventSpace, Teacher teacher, Long[][] schedule) {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.timeStart = timeStart;
        this.stringTime = new SimpleStringProperty(dateFormat.format(new Date(this.timeStart)));
        this.type = new SimpleObjectProperty<>(type);
        this.space = new SimpleObjectProperty<>(eventSpace);
        this.teacher = new SimpleObjectProperty<>(teacher);
        this.teacherFirstName = new SimpleStringProperty(teacher.getFirstName());
        this.teacherLastName = new SimpleStringProperty(teacher.getLastName());
        this.teacherPatronymic = new SimpleStringProperty(teacher.getPatronymic());
        this.schedule = schedule;
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Coterie getInstance(Integer id, String title, Long timeStart, CoterieType type, Space eventSpace,
                               Teacher teacher, Long[][] schedule) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Coterie(id, title, timeStart, type, eventSpace, teacher, schedule);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setTitle(title);
                    f.setSpace(eventSpace);
                    f.setTimeStart(timeStart);
                    f.setType(type);
                    f.setTeacher(teacher);
                    f.setSchedule(schedule);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Coterie coterie) {
        objectsList.remove(coterie);
        objectsId.remove(coterie.id);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setTimeStart(Long timeStart) {
       this.timeStart = timeStart;
       this.stringTime.setValue(dateFormat.format(new Date(timeStart)));
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public String getStringTime() {
        return stringTime.get();
    }

    public StringProperty stringTimeProperty() {
        return stringTime;
    }

    public CoterieType getType() {
        return type.getValue();
    }

    public void setType(CoterieType type) {
        this.type.setValue(type);
    }

    public Space getSpace() {
        return space.getValue();
    }

    public void setSpace(Space eventSpace) {
        this.space.setValue(eventSpace);
    }

    public Teacher getTeacher() {
        return teacher.getValue();
    }

    public void setTeacher(Teacher teacher) {
        teacherFirstName.set(teacher.getFirstName());
        teacherLastName.set(teacher.getLastName());
        teacherPatronymic.set(teacher.getPatronymic());
        this.teacher.setValue(teacher);
    }

    public String getTeacherFirstName() {
        return teacherFirstName.get();
    }

    public StringProperty teacherFirstNameProperty() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        getTeacher().setFirstName(teacherFirstName);
        this.teacherFirstName.set(teacherFirstName);
    }

    public String getTeacherLastName() {
        return teacherLastName.get();
    }

    public StringProperty teacherLastNameProperty() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        getTeacher().setLastName(teacherLastName);
        this.teacherLastName.set(teacherLastName);
    }

    public String getTeacherPatronymic() {
        return teacherPatronymic.get();
    }

    public StringProperty teacherPatronymicProperty() {
        return teacherPatronymic;
    }

    public void setTeacherPatronymic(String teacherPatronymic) {
        getTeacher().setPatronymic(teacherPatronymic);
        this.teacherPatronymic.set(teacherPatronymic);
    }

    public Long[][] getSchedule() {
        return schedule;
    }

    public void setSchedule(Long[][] schedule) {
        this.schedule = schedule;
    }
}