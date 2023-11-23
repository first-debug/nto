package com.vladislav.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

public class Task {
    private final LongProperty timeOfReg;
    private final Property<Event> event;
    private final StringProperty typeName;
    private final IntegerProperty spaceName;
    private final LongProperty deadline;
    private final StringProperty description;
    private final Property<Status> status;

    public Task(LongProperty timeOfReg, Property<Event> event,
                StringProperty typeName, IntegerProperty spaceName,
                LongProperty deadline, StringProperty description,
                Property<Status> status) {
        this.timeOfReg = timeOfReg;
        this.event = event;
        this.typeName = typeName;
        this.spaceName = spaceName;
        this.deadline = deadline;
        this.description = description;
        this.status = status;
    }

    public long getTimeOfReg() {
        return timeOfReg.get();
    }

    public Event getEvent() {
        return event.getValue();
    }

    public String getTypeName() {
        return typeName.get();
    }

    public int getSpaceName() {
        return spaceName.get();
    }

    public long getDeadline() {
        return deadline.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Status getStatus() {
        return status.getValue();
    }
}
