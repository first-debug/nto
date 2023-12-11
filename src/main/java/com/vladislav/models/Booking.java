package com.vladislav.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.dateFormat;

public class Booking {
    public final static ObservableList<Booking> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private final StringProperty timeOfRegString;
    private final Property<Event> event;
    private final StringProperty timeOfStartString;
    private final StringProperty timeOfEndString;
    private final Property<Space> space;
    private final IntegerProperty halfOfSpace; // от 1 до 3; 1-первая, 2-вторая, 3-полностью
    private final StringProperty comment;
    private Long timeOfReg;
    private Long timeOfStart;
    private Long timeOfEnd;

    private Booking(Integer id, Long timeOfReg, Event event, Long timeOfStart,
                    Long timeOfEnd, Space space, Integer halfOfSpace, String comment) {
        this.id = id;
        this.timeOfReg = timeOfReg;
        this.timeOfRegString = new SimpleStringProperty(dateFormat.format(new Date(timeOfReg)));
        this.event = new SimpleObjectProperty<>(event);
        this.timeOfStart = timeOfStart;
        this.timeOfStartString = new SimpleStringProperty(dateFormat.format(new Date(timeOfStart)));
        this.timeOfEnd = timeOfEnd;
        this.timeOfEndString = new SimpleStringProperty(dateFormat.format(new Date(timeOfEnd)));
        this.space = new SimpleObjectProperty<>(space);
        this.halfOfSpace = new SimpleIntegerProperty(halfOfSpace);
        this.comment = new SimpleStringProperty(comment);
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Booking getInstance(Integer id, Long timeOfReg, Event event, Long timeOfStart,
                                      Long timeOfEnd, Space space, Integer halfOfSpace, String comment) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Booking(id, timeOfReg, event, timeOfStart,
                    timeOfEnd, space, halfOfSpace, comment);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setTimeOfReg(timeOfReg);
                    f.setEvent(event);
                    f.setTimeOfStart(timeOfStart);
                    f.setTimeOfEnd(timeOfEnd);
                    f.setSpace(space);
                    f.setHalfOfSpace(halfOfSpace);
                    f.setComment(comment);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Booking booking) {
        objectsList.remove(booking);
        objectsId.remove(booking.id);
    }

    public Integer getId() {
        return id;
    }

    public Long getTimeOfReg() {
        return timeOfReg;
    }

    public void setTimeOfReg(Long timeOfReg) {
        this.timeOfReg = timeOfReg;
    }

    public Event getEvent() {
        return event.getValue();
    }

    public void setEvent(Event event) {
        this.event.setValue(event);
    }

    public Property<Event> eventProperty() {
        return event;
    }

    public Long getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(Long timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public Long getTimeOfEnd() {
        return timeOfEnd;
    }

    public void setTimeOfEnd(Long timeOfEnd) {
        this.timeOfEnd = timeOfEnd;
    }

    public Space getSpace() {
        return space.getValue();
    }

    public void setSpace(Space space) {
        this.space.setValue(space);
    }

    public Property<Space> spaceProperty() {
        return space;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public Integer getHalfOfSpace() {
        return halfOfSpace.get();
    }

    public void setHalfOfSpace(Integer halfOfSpace) {
        this.halfOfSpace.set(halfOfSpace);
    }

    public String getTimeOfRegString() {
        return timeOfRegString.get();
    }

    public void setTimeOfRegString(String timeOfRegString) {
        this.timeOfRegString.set(timeOfRegString);
    }

    public StringProperty timeOfRegStringProperty() {
        return timeOfRegString;
    }

    public String getTimeOfStartString() {
        return timeOfStartString.get();
    }

    public void setTimeOfStartString(String timeOfStartString) {
        this.timeOfStartString.set(timeOfStartString);
    }

    public StringProperty timeOfStartStringProperty() {
        return timeOfStartString;
    }

    public String getTimeOfEndString() {
        return timeOfEndString.get();
    }

    public void setTimeOfEndString(String timeOfEndString) {
        this.timeOfEndString.set(timeOfEndString);
    }

    public StringProperty timeOfEndStringProperty() {
        return timeOfEndString;
    }
}
