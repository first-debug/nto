package com.vladislav.models;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.format;

public class Task {
    public final static  ArrayList<Task> objectsList = new ArrayList<>();

    private final Integer id;
    private final Long timeOfReg;
    private final StringProperty timeOfRegString;
    private final Property<TaskType> type;
    private final Property<Event> event;
    private final Property<Space> space;
    private final LongProperty deadline;
    private final StringProperty deadlineString;
    private final StringProperty description;
    private Status status;

    public Task(Integer id, Long timeOfReg, Event event,
                TaskType type, Long deadline, Status status) {

        this.id = id;
        this.timeOfReg = timeOfReg;
        this.timeOfRegString = new SimpleStringProperty(format.format(new Date(this.timeOfReg)));
        this.event = new SimpleObjectProperty<>(event);
        this.type = new SimpleObjectProperty<>(type);
        this.space = new SimpleObjectProperty<>(event.getSpace());
        this.deadline = new SimpleLongProperty(deadline);
        this.description = new SimpleStringProperty(type.getDescription());
        this.status = status;
        this.deadlineString = new SimpleStringProperty(format.format(new Date(deadline)));
        Task.objectsList.add(this);
    }

    public void setCompleted() {
        status = Status.COMPLETED;
    }

    public Integer getId() {
        return id;
    }

    public Long getTimeOfReg() {
        return timeOfReg;
    }

    public Event getEvent() {
        return event.getValue();
    }

    public Property<Event> EventProperty() {
        return event;
    }

    public TaskType getType() {
        return type.getValue();
    }

    public Property<TaskType> TaskTypeProperty() {
        return type;
    }

    public Space getSpace() {
        return space.getValue();
    }

    public Property<Space> SpaceProperty() {
        return space;
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

    public String getTimeOfRegName() {
        return timeOfRegString.get();
    }

    @Override
    public String toString() {
        return "Task{" +
                this.hashCode() +
                " id=" + id +
                ", timeOfReg=" + timeOfReg +
                ", timeOfRegString=" + timeOfRegString +
                ", type=" + type +
                ", event=" + event +
                ", space=" + space.getName() +
                ", deadline=" + deadline +
                ", deadlineString=" + deadlineString +
                ", description=" + description +
                ", status=" + status +
                '}';
    }
}
