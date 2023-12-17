package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.controllers.admin.*;
import javafx.fxml.FXML;

public class AdminDesktopController extends Controller {

    @FXML
    void toEvents() {
        App.setRoot("allEvents", new AdminAllEventsController());
    }

    @FXML
    void toAddEvents() {
        App.setRoot("createNewEvent", new AdminCreateNewEventController());
    }

    @FXML
    void toTasks() {
        App.setRoot("allTasks", new AdminAllTasksController());
    }

    @FXML
    void toAddTask() {
        App.setRoot("createNewTask", new AdminCreateNewTaskController());
    }

    @FXML
    void toSpaces() {
        App.setRoot("allSpaces", new AdminAllSpacesController());
    }

    @FXML
    void toAddSpace() {
        App.setRoot("createNewSpace", new AdminCreateNewSpaceController());
    }

    @FXML
    void toAddBooking() {
        App.setRoot("createNewBooking", new AdminCreateNewBookingController());
    }

    @FXML
    void toEditLessons() {
        App.setRoot("editLesson", new AdminEditLessonsController());
    }

    @FXML
    void toLessons() {
        App.setRoot("allLessons", new AdminLessonsController());
    }
}


