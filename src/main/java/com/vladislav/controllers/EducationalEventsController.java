package com.vladislav.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class EducationalEventsController extends EventTablesController implements Initializable {
    
    @FXML
    private Button goBackButton;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        createEventsTable(false);
    }
}
