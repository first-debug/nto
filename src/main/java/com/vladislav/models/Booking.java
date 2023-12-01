package com.vladislav.models;

import javafx.beans.property.*;

public class Booking {

    private final Integer id;
    private final LongProperty timeOfReg;
    private final Property<Event> event;
    private final Long timeOfStart;
    private final Long timeOfEnd;
    private final Property<Space> space;
    private Integer halfOfSpace; // от 1 до 3; 1-первая, 2-вторая, 3-полностью
    private final StringProperty comment;

    public Booking(Integer id, Long timeOfReg, Event event, Long timeOfStart,
                   Long timeOfEnd, Space space, Integer halfOfSpace, String comment) {
        this.id = id;
        this.timeOfReg = new SimpleLongProperty(timeOfReg);
        this.event = new SimpleObjectProperty<>(event);
        this.timeOfStart = timeOfStart;
        this.timeOfEnd = timeOfEnd;
        this.space = new SimpleObjectProperty<>(space);
        this.halfOfSpace = halfOfSpace;
        this.comment = new SimpleStringProperty(comment);
    }

    public Integer getId() {
        return id;
    }

    public long getTimeOfReg() {
        return timeOfReg.get();
    }

    public LongProperty timeOfRegProperty() {
        return timeOfReg;
    }

    public void setTimeOfReg(long timeOfReg) {
        this.timeOfReg.set(timeOfReg);
    }

    public Event getEvent() {
        return event.getValue();
    }

    public Property<Event> eventProperty() {
        return event;
    }

    public void setEvent(Event event) {
        this.event.setValue(event);
    }

    public Long getTimeOfStart() {
        return timeOfStart;
    }

    public Long getTimeOfEnd() {
        return timeOfEnd;
    }

    public Space getSpace() {
        return space.getValue();
    }

    public Property<Space> spaceProperty() {
        return space;
    }

    public void setSpace(Space space) {
        this.space.setValue(space);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public Integer getHalfOfSpace() {
        return halfOfSpace;
    }

    public void setHalfOfSpace(Integer halfOfSpace) {
        this.halfOfSpace = halfOfSpace;
    }
}
