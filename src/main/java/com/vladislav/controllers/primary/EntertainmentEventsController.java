package com.vladislav.controllers.primary;

import com.vladislav.controllers.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class EntertainmentEventsController extends EventTablesController implements Initializable {

    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        createEventsTable(true, "Развлечения");
    }
}