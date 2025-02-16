package com.vladislav.presentation;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.admin.*;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminDesktopController extends Controller {

    public AdminDesktopController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    @FXML
    void toEvents() {
        applicationService.changeRootStage("allEvents", new AdminAllEventsController(applicationService));
    }

    @FXML
    void toAddEvents() {
        applicationService.changeRootStage("createNewEvent", new AdminCreateNewEventController(applicationService));
    }

    @FXML
    void toTasks() {
        applicationService.changeRootStage("allTasks", new AdminAllTasksController(applicationService));
    }

    @FXML
    void toAddTask() {
        applicationService.changeRootStage("createNewTask", new AdminCreateNewTaskController(applicationService));
    }

    @FXML
    void toSpaces() {
        applicationService.changeRootStage("allSpaces", new AdminAllSpacesController(applicationService));
    }

    @FXML
    void toAddSpace() {
        applicationService.changeRootStage("createNewSpace", new AdminCreateNewSpaceController(applicationService));
    }

    @FXML
    void toAddBooking() {
        applicationService.changeRootStage("createNewBooking", new AdminCreateNewBookingController(applicationService));
    }

    @FXML
    void toEditLessons() {
        applicationService.changeRootStage("editLesson", new AdminEditLessonsController(applicationService));
    }

    @FXML
    void toLessons() {
        applicationService.changeRootStage("allLessons", new AdminLessonsController(applicationService));
    }
}


