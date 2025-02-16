package com.vladislav.domain;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.domain.DateFormatRepo.dateFormat;
import static com.vladislav.domain.DateFormatRepo.timeFormat;

public class Lesson {
    public static final ObservableList<Lesson> objectsList = FXCollections.observableArrayList();
    public static final ArrayList<Integer> objectsId = new ArrayList<>();
    private final Integer id;
    private final StringProperty title;
    private final StringProperty stringTime;
    private final Property<LessonType> type;
    private final Property<Space> space;
    private final Property<Teacher> teacher;
    private final StringProperty teacherFirstName;
    private final StringProperty teacherLastName;
    private final StringProperty teacherPatronymic;
    private Long timeStart;
    private Long[][] schedule;

    private Lesson(Integer id, String title, Long timeStart, LessonType type, Space eventSpace, Teacher teacher, Long[][] schedule) {
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

    public static Lesson getInstance(Integer id, String title, Long timeStart, LessonType type, Space eventSpace,
                                     Teacher teacher, Long[][] schedule) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Lesson(id, title, timeStart, type, eventSpace, teacher, schedule);
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

    public static void remove(Lesson lesson) {
        objectsList.remove(lesson);
        objectsId.remove(lesson.id);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
        this.stringTime.setValue(dateFormat.format(new Date(timeStart)));
    }

    public String getStringTime() {
        return stringTime.get();
    }

    public StringProperty stringTimeProperty() {
        return stringTime;
    }

    public LessonType getType() {
        return type.getValue();
    }

    public void setType(LessonType type) {
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

    public void setTeacherFirstName(String teacherFirstName) {
        getTeacher().setFirstName(teacherFirstName);
        this.teacherFirstName.set(teacherFirstName);
    }

    public StringProperty teacherFirstNameProperty() {
        return teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName.get();
    }

    public void setTeacherLastName(String teacherLastName) {
        getTeacher().setLastName(teacherLastName);
        this.teacherLastName.set(teacherLastName);
    }

    public StringProperty teacherLastNameProperty() {
        return teacherLastName;
    }

    public String getTeacherPatronymic() {
        return teacherPatronymic.get();
    }

    public void setTeacherPatronymic(String teacherPatronymic) {
        getTeacher().setPatronymic(teacherPatronymic);
        this.teacherPatronymic.set(teacherPatronymic);
    }

    public StringProperty teacherPatronymicProperty() {
        return teacherPatronymic;
    }

    public Long[][] getSchedule() {
        return schedule;
    }

    public StringProperty getScheduleDay(int day) {
        String text = null;
        if (schedule[day][0] != -1) {
            text = timeFormat.format(new Date(schedule[day][0])) + " - " +
                    timeFormat.format(new Date(schedule[day][1])) + "\n" +
                    space.getValue().getName() + "\n" +
                    teacher.getValue().getAbbreviatedName();
        }
        return new SimpleStringProperty(text);
    }

    public void setSchedule(Long[][] schedule) {
        this.schedule = schedule;
    }
}