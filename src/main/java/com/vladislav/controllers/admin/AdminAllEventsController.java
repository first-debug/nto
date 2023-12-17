package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminAllEventsController extends EventTablesController implements Initializable {

    @FXML
    public void switchToPrimary() {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable(null, "Все меороприятия");
        loginWindowButton.setVisible(false);
    }
}
