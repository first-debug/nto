package com.vladislav.models;

import javafx.beans.property.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {


    private final Integer id;
    private final StringProperty title;
    private final StringProperty description;
    private final Space space;
    private final StringProperty spaceName;
    private final LongProperty timeToStart;
    private final StringProperty stringTime;
    private final EventType eventType;
    private final StringProperty typeName;
    private final boolean isEntertainment;

    public Event(Integer id, String title, String description, Space space, Long timeToStart, EventType eventType)
    {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.space = space;
        this.spaceName = new SimpleStringProperty(this.space.getName());
        this.timeToStart = new SimpleLongProperty(timeToStart);
        this.eventType = eventType;
        this.typeName = new SimpleStringProperty(eventType.getName());
        this.isEntertainment = eventType.getIsEntertainment();
        
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        this.stringTime = new SimpleStringProperty(format.format(new Date(timeToStart)));
    }

    public String getTitle()
    {
        return title.get();
    }

    public String getDescription() {
		return description.get();
	}

    public Space getSpace() {
        return space;
    }

    public String getSpaceName() {
        return spaceName.get();
    }

    public Long getTimeToStart() {
		return timeToStart.get();
	}

	public EventType getType() {
		return eventType;
	}

    public String getTypeName() {
        return typeName.get();
    }

    public String getStringTime() {
        return stringTime.get();
    }

    public boolean getIsEntertainment() {
        return isEntertainment;
    }

    public Integer getId() {
        return id;
    }
}