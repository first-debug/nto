package com.vladislav.controllers;

import com.vladislav.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminAllEventsController extends EventTablesController implements Initializable {

    @FXML
    @Override
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable();
    }
}
