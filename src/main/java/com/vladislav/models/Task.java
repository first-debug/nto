package com.vladislav.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.dateFormat;

public class Task {
    public final static ObservableList<Task> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private Long timeOfReg;
    private final StringProperty timeOfRegString;
    private final Property<TaskType> type;
    private final Property<Event> event;
    private final Property<Space> space;
    private final LongProperty deadline;
    private final StringProperty deadlineString;
    private final StringProperty description;
    private Status status;

    private Task(Integer id, Long timeOfReg, Event event,
                TaskType type, Long deadline, Status status) {

        this.id = id;
        this.timeOfReg = timeOfReg;
        this.timeOfRegString = new SimpleStringProperty(dateFormat.format(new Date(this.timeOfReg)));
        this.event = new SimpleObjectProperty<>(event);
        this.type = new SimpleObjectProperty<>(type);
        this.space = new SimpleObjectProperty<>(event.getSpace());
        this.deadline = new SimpleLongProperty(deadline);
        this.description = new SimpleStringProperty(type.getDescription());
        this.status = status;
        this.deadlineString = new SimpleStringProperty(dateFormat.format(new Date(deadline)));
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Task getInstant(Integer id, Long timeOfReg, Event event,
                                  TaskType type, Long deadline, Status status) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Task(id, timeOfReg, event, type, deadline, status);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setTimeOfReg(timeOfReg);
                    f.setEvent(event);
                    f.setType(type);
                    f.setDeadline(deadline);
                    f.setStatus(status);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Task task) {
        objectsId.remove(task.id);
        objectsList.remove(task);
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

    public void setTimeOfReg(Long timeOfReg) {
        this.timeOfReg = timeOfReg;
    }

    public void setTimeOfRegString(String timeOfRegString) {
        this.timeOfRegString.set(timeOfRegString);
    }

    public void setType(TaskType type) {
        this.type.setValue(type);
    }

    public void setEvent(Event event) {
        this.event.setValue(event);
    }

    public void setSpace(Space space) {
        this.space.setValue(space);
    }

    public void setDeadline(long deadline) {
        this.deadline.set(deadline);
    }

    public void setDeadlineString(String deadlineString) {
        this.deadlineString.set(deadlineString);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setStatus(Status status) {
        this.status = status;
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
