package com.vladislav.controllers;

import com.vladislav.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminDesktopController extends Controller {

    @FXML
    void toEvents() throws IOException {
        App.setRoot("adminAllEvents");
    }

    @FXML
    void toEditEvents() throws IOException {
        App.setRoot("adminCreateNewEvent");
    }

    @FXML
    void toAddTask() throws IOException {
        App.setRoot("editTasks");
    }

    @FXML
    void toTasks() throws IOException {
        App.setRoot("adminAllTasks");
    }

    @FXML
    void toSpaces() throws IOException {
        App.setRoot("adminAllSpaces");
    }
}
