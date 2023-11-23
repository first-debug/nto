package com.vladislav.controllers;



import java.io.IOException;

import com.vladislav.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class Controller {

    @FXML
    private MenuBar menuBar;

    @FXML
    private void exit(ActionEvent event)
    {
        App.exit();
    }

    @FXML
    private void showDocumentation(ActionEvent event) throws IOException
    {
        App.setRoot("about");
    }

    @FXML
    private void edit(ActionEvent event) {
        System.out.print(event);
    }

    @FXML
    private void editEvents(ActionEvent event) throws IOException {
        App.setRoot("createNewEvent");
    }
}
