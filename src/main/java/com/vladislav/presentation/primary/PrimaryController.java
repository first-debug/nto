package com.vladislav.presentation.primary;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.Controller;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class PrimaryController extends Controller {

    public PrimaryController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    @FXML
    private void switchToEventsList() {
        applicationService.changeRootStage("allEvents", new AllEventsController(applicationService));
    }

    @FXML
    private void switchToEntertainments() {
        applicationService.changeRootStage("allEvents", new EntertainmentEventsController(applicationService));
    }

    @FXML
    private void switchToEducation() {
        applicationService.changeRootStage("allEvents", new EducationalEventsController(applicationService));
    }

    @FXML
    private void switchToTeaching() {
        applicationService.changeRootStage("allEvents", new TeachingEventsController(applicationService));
    }

    @FXML
    private void switchToLessonsList() {
        applicationService.changeRootStage("allLessons", new LessonsController(applicationService));
    }
}
