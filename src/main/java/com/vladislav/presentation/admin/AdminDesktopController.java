package com.vladislav.presentation.admin;

import com.vladislav.presentation.Controller;
import com.vladislav.presentation.WindowService;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminDesktopController extends Controller {

    public AdminDesktopController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    void toEvents() {
        windowService.changeRootStage("allEvents", new AdminAllEventsController(windowService));
    }

    @FXML
    void toAddEvents() {
        windowService.changeRootStage("createNewEvent", new AdminCreateNewEventController(windowService));
    }

    @FXML
    void toTasks() {
        windowService.changeRootStage("allTasks", new AdminAllTasksController(windowService));
    }

    @FXML
    void toAddTask() {
        windowService.changeRootStage("createNewTask", new AdminCreateNewTaskController(windowService));
    }

    @FXML
    void toSpaces() {
        windowService.changeRootStage("allSpaces", new AdminAllSpacesController(windowService));
    }

    @FXML
    void toAddSpace() {
        windowService.changeRootStage("createNewSpace", new AdminCreateNewSpaceController(windowService));
    }

    @FXML
    void toAddBooking() {
        windowService.changeRootStage("createNewBooking", new AdminCreateNewBookingController(windowService));
    }

    @FXML
    void toEditLessons() {
        windowService.changeRootStage("editLesson", new AdminEditLessonsController(windowService));
    }

    @FXML
    void toLessons() {
        windowService.changeRootStage("allLessons", new AdminLessonsController(windowService));
    }
}


