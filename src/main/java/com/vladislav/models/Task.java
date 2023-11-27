package com.vladislav.models;

import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private final Integer id;
    private final Long timeOfReg;
    private final StringProperty timeOfRegName;
    private final TaskType type;
    private final StringProperty typeName;
    private final Event event;
    private final StringProperty eventName;
    private final Space space;
    private final StringProperty spaceName;
    private final LongProperty deadline;
    private final StringProperty deadlineString;
    private final StringProperty description;
    private Status status;
    private StringProperty statusName;

    public Task(Integer id, Long timeOfReg, Event event,
                TaskType type, Long deadline, Status status) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        this.id = id;
        this.timeOfReg = timeOfReg;
        this.timeOfRegName = new SimpleStringProperty(format.format(new Date(this.timeOfReg)));
        this.event = event;
        this.eventName = new SimpleStringProperty(event.getTitle());
        this.type = type;
        this.typeName = new SimpleStringProperty(type.getName());
        this.space = event.getSpace();
        this.spaceName = new SimpleStringProperty(this.space.getName());
        this.deadline = new SimpleLongProperty(deadline);
        this.description = new SimpleStringProperty(this.type.getDescription());
        this.status = status;
        this.statusName = new SimpleStringProperty(status.getName());
        this.deadlineString = new SimpleStringProperty(format.format(new Date(deadline)));
    }

    public void setCompleted() {
        status = Status.COMPLETED;
        statusName = new SimpleStringProperty(status.getName());
    }

    public Integer getId() {
        return id;
    }

    public Long getTimeOfReg() {
        return timeOfReg;
    }

    public Event getEvent() {
        return event;
    }

    public String getEventName() {
        return eventName.get();
    }

    public TaskType getType() {
        return type;
    }

    public String getTypeName() {
        return typeName.get();
    }

    public Space getSpace() {
        return space;
    }

    public String getSpaceName() {
        return spaceName.get();
    }

    public Long getDeadline() {
        return deadline.get();
    }

    public String getDeadlineString() {
        return deadlineString.get();
    }

    public String getDescription() {
        return description.get();
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName.get();
    }

    public String getTimeOfRegName() {
        return timeOfRegName.get();
    }
}
