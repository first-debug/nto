package com.vladislav;

import java.io.IOException;

import com.vladislav.models.Event;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class PrimaryController {
    
    @FXML
    private TableView<Event> tableOfEvents;

    @FXML
    private void switchToEntertainments() throws IOException
    {
        App.setRoot("entertainments");
    }

    @FXML
    private void switchToEducation() throws IOException
    {
        App.setRoot("education");
    }

    @FXML
    private void switchToTeaching() throws IOException
    {
        App.setRoot("teaching");
    }

    @FXML
    private void exit()
    {
        Platform.exit();
    }
}
