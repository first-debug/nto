package com.vladislav.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Event {
    
    private StringProperty title;
    private StringProperty description;
    private StringProperty timeToStart;
    private StringProperty type;

    public Event(String title, String description, String timeToStart, Type type)
    {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.timeToStart = new SimpleStringProperty(timeToStart);
        this.type = new SimpleStringProperty(type.getName());
    }

    public String getTitle()
    {
        return title.get();
    }

    public String getDescription() {
		return description.get();
	}

	public String getTimeToStart() {
		return timeToStart.get();
	}

	public String getType() {
		return type.get();
	}
}