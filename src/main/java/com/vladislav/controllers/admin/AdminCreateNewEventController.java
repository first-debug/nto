package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.primary.CreateNewEventController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;

public class AdminCreateNewEventController extends CreateNewEventController implements Initializable {
    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }
}
