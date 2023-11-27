package com.vladislav.controllers;



import java.io.IOException;

import com.vladislav.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {

    @FXML
    private void exit() {
        App.exit();
    }

    @FXML
    private void closeWindow() {
        App.closeSecondWindow();
    }

    @FXML
    private void showDocumentation() throws IOException {
        App.newSecondWindow("about", "About");
    }

    @FXML
    private void switchToLoginWindow() throws IOException {
        App.setRoot("loginForSE");
    }

    @FXML
    private void editEvents() throws IOException {
        App.setRoot("createNewEvent");
    }

    @FXML
    void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
