package com.vladislav.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.vladislav.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class EntertainmentsController extends TablesController implements Initializable {
    
    @FXML
    private Button goBackButton;

    @FXML
    private void switchToPrimary(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        createEventsTable();
    }
}