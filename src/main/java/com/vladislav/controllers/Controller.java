package com.vladislav.controllers;



import java.io.IOException;

import com.vladislav.App;

import com.vladislav.controllers.primary.CreateNewEventController;
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
    private void showDocumentation() throws IOException {
        App.newSecondWindow("about", new AboutController(),"About");
    }

    @FXML
    private void switchToLoginWindow() {
        App.setRoot("loginForSE", new LoginForSEController());
    }

    @FXML
    private void editEvents() {
        App.setRoot("createNewEvent", new CreateNewEventController());
    }

    @FXML
    void switchToPrimary() {
        App.setRoot("primary", new PrimaryController());
    }
}
