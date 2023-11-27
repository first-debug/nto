package com.vladislav.controllers;

import java.io.IOException;

import com.vladislav.App;

import javafx.fxml.FXML;

public class PrimaryController extends Controller {

    @FXML
    private void switchToEventsList() throws IOException {
        App.setRoot("allEvents");
    }

    @FXML
    private void switchToEntertainments() throws IOException {
        App.setRoot("entertainment");
    }

    @FXML
    private void switchToEducation() throws IOException {
        App.setRoot("educational");
    }

    @FXML
    private void switchToTeaching() throws IOException {
        App.setRoot("teaching");
    }

    @FXML
    private void switchToEditEvents() throws IOException {
        App.setRoot("createNewEvent");
    }
}
