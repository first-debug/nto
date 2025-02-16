package com.vladislav.application.events;

import com.vladislav.presentation.Controller;
import org.springframework.context.ApplicationEvent;

public class CreateSecondWindowEvent extends ApplicationEvent {

    private final String fxml;
    private final Controller controller;
    private final String title;
    private final Integer width;
    private final Integer height;

    public CreateSecondWindowEvent(String fxml, Controller controller, String title, Integer width, Integer height) {
        super(controller);
        this.fxml = fxml;
        this.controller = controller;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getFxml() {
        return fxml;
    }

    public Controller getController() {
        return controller;
    }

    public String getTitle() {
        return title;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
