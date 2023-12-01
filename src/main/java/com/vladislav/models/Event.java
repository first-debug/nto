package com.vladislav.models;

import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vladislav.App.format;

public class Event {
    public final static ArrayList<Event> objectsList = new ArrayList<>();

    private final Integer id;
    private final StringProperty title;
    private final StringProperty description;
    private final Property<Space> space;
    private final LongProperty timeToStart;
    private final StringProperty stringTime;
    private final Property<EventType> eventType;
    private final boolean isEntertainment;

    public Event(Integer id, String title, String description, Space space, Long timeToStart, EventType eventType)
    {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.space = new SimpleObjectProperty<>(space);
        this.timeToStart = new SimpleLongProperty(timeToStart);
        this.eventType = new SimpleObjectProperty<>(eventType);
        this.isEntertainment = eventType.getIsEntertainment();
        this.stringTime = new SimpleStringProperty(format.format(new Date(timeToStart)));
        Event.objectsList.add(this);
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
}