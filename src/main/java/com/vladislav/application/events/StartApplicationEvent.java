package com.vladislav.application.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class StartApplicationEvent extends ApplicationEvent {
    public StartApplicationEvent(Object source) {
        super(source);
    }

    public Stage getStage() {
        return (Stage) getSource();
    }
}
