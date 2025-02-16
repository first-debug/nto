package com.vladislav.presentation.admin;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.AdminDesktopController;
import com.vladislav.presentation.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminAllEventsController extends EventTablesController implements Initializable {

    public AdminAllEventsController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    @FXML
    public void switchToPrimary() {
        applicationService.changeRootStage("adminDesktop", new AdminDesktopController(applicationService));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable(null, "Все меороприятия");
        loginWindowButton.setVisible(false);
    }
}
