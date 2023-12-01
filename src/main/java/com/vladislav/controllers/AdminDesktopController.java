package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.controllers.admin.*;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminDesktopController extends Controller {

    @FXML
    void toEvents() {
        App.setRoot("allEvents", new AdminAllEventsController());
    }

    @FXML
    void toEditEvents() {
        App.setRoot("createNewEvent", new AdminCreateNewEventController());
    }

    @FXML
    void toAddTask() {
        App.setRoot("createNewTask", new AdminCreateNewTaskController());
    }

    @FXML
    void toTasks() {
        App.setRoot("allTasks", new AdminAllTasksController());
    }

    @FXML
    void toSpaces() {
        App.setRoot("allSpaces", new AdminAllSpacesController());
    }
}
