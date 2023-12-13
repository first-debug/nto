package com.vladislav.controllers.primary;

import com.vladislav.App;
import com.vladislav.controllers.Controller;
import javafx.fxml.FXML;

public class PrimaryController extends Controller {

    @FXML
    private void switchToEventsList() {
        App.setRoot("allEvents", new AllEventsController());
    }

    @FXML
    private void switchToEntertainments() {
        App.setRoot("allEvents", new EntertainmentEventsController());
    }

    @FXML
    private void switchToEducation() {
        App.setRoot("allEvents", new EducationalEventsController());
    }

    @FXML
    private void switchToTeaching() {
        App.setRoot("allEvents", new TeachingEventsController());
    }

    @FXML
    private void switchToLessonsList() {
        App.setRoot("allLessons", new LessonsController());
    }
}
