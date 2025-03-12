package com.vladislav.presentation.admin;

import com.vladislav.presentation.services.WindowService;
import com.vladislav.presentation.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminAllEventsController extends EventTablesController implements Initializable {

    public AdminAllEventsController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    public void switchToPrimary() {
        windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable(null, "Все мероприятия");
        loginWindowButton.setVisible(false);
    }
}
