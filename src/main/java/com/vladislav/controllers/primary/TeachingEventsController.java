package com.vladislav.controllers.primary;

import java.net.URL;
import java.util.ResourceBundle;

import com.vladislav.controllers.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class TeachingEventsController extends EventTablesController implements Initializable {
    
    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        createEventsTable(false, "Образование");
    }
}
