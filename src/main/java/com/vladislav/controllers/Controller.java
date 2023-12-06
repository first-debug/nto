package com.vladislav.controllers;

import com.vladislav.App;

import com.vladislav.controllers.primary.PrimaryController;
import javafx.fxml.FXML;

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
    private void showDocumentation() {
        App.newWindow("about", new AboutController(),"About", 720, 540);
    }

    @FXML
    private void switchToLoginWindow() {
        App.setRoot("loginForSE", new LoginForSEController());
    }

    @FXML
    private void switchToPrimary() {
        App.setRoot("primary", new PrimaryController());
    }
}
