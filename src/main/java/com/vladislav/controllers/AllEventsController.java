package com.vladislav.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AllEventsController extends EventTablesController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createEventsTable();
    }
}
