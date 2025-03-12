package com.vladislav.presentation.primary;

import com.vladislav.presentation.services.WindowService;
import com.vladislav.presentation.Controller;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class PrimaryController extends Controller {

    public PrimaryController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    private void switchToEventsList() {
        windowService.changeRootStage("allEvents", new AllEventsController(windowService));
    }

    @FXML
    private void switchToEntertainments() {
        windowService.changeRootStage("allEvents", new EntertainmentEventsController(windowService));
    }

    @FXML
    private void switchToEducation() {
        windowService.changeRootStage("allEvents", new EducationalEventsController(windowService));
    }

    @FXML
    private void switchToTeaching() {
        windowService.changeRootStage("allEvents", new TeachingEventsController(windowService));
    }

    @FXML
    private void switchToLessonsList() {
        windowService.changeRootStage("allLessons", new LessonsController(windowService));
    }
}
