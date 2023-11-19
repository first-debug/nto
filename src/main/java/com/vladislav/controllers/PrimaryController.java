package com.vladislav.controllers;

import java.io.IOException;

import com.vladislav.App;

import javafx.fxml.FXML;

public class PrimaryController extends Controller{

    @FXML
    private void switchToEntertainments() throws IOException
    {
        App.setRoot("entertainments");
    }

    @FXML
    private void switchToEducation() throws IOException
    {
        App.setRoot("education");
    }

    @FXML
    private void switchToTeaching() throws IOException
    {
        App.setRoot("teaching");
    }
}
