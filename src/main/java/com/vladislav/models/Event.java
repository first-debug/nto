package com.vladislav.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    
    private final StringProperty title;
    private final StringProperty description;
    private final LongProperty timeToStart;
    private final StringProperty stringTime;
    private final StringProperty type;

    public Event(String title, String description, Long timeToStart, String type)
    {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.timeToStart = new SimpleLongProperty(timeToStart);
        this.type = new SimpleStringProperty(type);
        
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

	public Long getTimeToStart() {
		return timeToStart.get();
	}

	public String getType() {
		return type.get();
	}

    public String getStringTime() {
        return stringTime.get();
    }
}