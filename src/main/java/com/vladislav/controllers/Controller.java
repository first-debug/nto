package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.controllers.primary.PrimaryController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.Calendar;

public class Controller {

    @FXML
    protected MenuItem loginWindowButton;

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
        App.newWindow("about", new AboutController(), "About", 720, 540);
    }

    @FXML
    private void switchToLoginWindow() {
        App.setRoot("loginForSE", new LoginForSEController());
    }

    @FXML
    private void switchToPrimary() {
        App.setRoot("primary", new PrimaryController());
    }

    protected Calendar parseDate(LocalDate date, String hourString, String minuteString) {
        Calendar.Builder dateBuilder = new Calendar.Builder();
        String[] dateSet = date.toString().split("-");
        int year = Integer.parseInt(dateSet[0]);
        int month = Integer.parseInt(dateSet[1]);
        int day = Integer.parseInt(dateSet[2]);
        int hour;
        if (hourString == null) hour = 0;
        else hour = Integer.parseInt(hourString);
        int minute;
        if (minuteString == null) minute = 0;
        else minute = Integer.parseInt(minuteString);
        dateBuilder.setDate(year, month - 1, day);
        dateBuilder.setTimeOfDay(hour, minute, 0);
        return dateBuilder.build();
    }
}
