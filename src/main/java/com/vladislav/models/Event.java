package com.vladislav.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.dateFormat;

public class Event {
    public final static ObservableList<Event> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final Integer id;
    private final StringProperty title;
    private final StringProperty description;
    private final Property<Space> space;
    private final LongProperty timeToStart;
    private final StringProperty stringTime;
    private final Property<EventType> eventType;
    private final boolean isEntertainment;

    private Event(Integer id, String title, String description, Space space, Long timeToStart, EventType eventType)
    {

        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.space = new SimpleObjectProperty<>(space);
        this.timeToStart = new SimpleLongProperty(timeToStart);
        this.eventType = new SimpleObjectProperty<>(eventType);
        this.isEntertainment = eventType.getIsEntertainment();
        this.stringTime = new SimpleStringProperty(dateFormat.format(new Date(timeToStart)));
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Event getInstance(Integer id, String title, String description, Space space, Long timeToStart, EventType eventType) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Event(id, title, description, space, timeToStart, eventType);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setTitle(title);
                    f.setDescription(description);
                    f.setSpace(space);
                    f.setTimeToStart(timeToStart);
                    f.setEventType(eventType);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Event event) {
        objectsList.remove(event);
        objectsId.remove(event.id);
    }

    public String getTitle()
    {
        return title.get();
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public String getDescription() {
		return description.get();
	}

    public StringProperty descriptionProperty() {
        return description;
    }

    public Space getSpace() {
        return space.getValue();
    }

    public Long getTimeToStart() {
		return timeToStart.get();
	}

	public EventType getType() {
		return eventType.getValue();
	}

    public String getStringTime() {
        return stringTime.get();
    }

    public LongProperty timeToStartProperty() {
        return timeToStart;
    }

    public boolean getIsEntertainment() {
        return isEntertainment;
    }

    public StringProperty stringTimeProperty() {
        return stringTime;
    }

    public Integer getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setSpace(Space space) {
        this.space.setValue(space);
    }

    public void setTimeToStart(long timeToStart) {
        this.timeToStart.set(timeToStart);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        this.stringTime.set(format.format(new Date(timeToStart)));
    }

    public void setEventType(EventType eventType) {
        this.eventType.setValue(eventType);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", space=" + space +
                ", timeToStart=" + timeToStart +
                ", stringTime=" + stringTime +
                ", eventType=" + eventType +
                ", isEntertainment=" + isEntertainment +
                '}';
    }
}