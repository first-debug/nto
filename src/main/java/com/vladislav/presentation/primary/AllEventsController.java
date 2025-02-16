package com.vladislav.presentation.primary;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.EventTablesController;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class AllEventsController extends EventTablesController implements Initializable {
    public AllEventsController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable(null, "Все мероприятия");
    }
}
