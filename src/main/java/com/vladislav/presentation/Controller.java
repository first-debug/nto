package com.vladislav.presentation;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.primary.PrimaryController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;

@Component
public class Controller {

    protected final ApplicationService applicationService;

    @FXML
    protected MenuItem loginWindowButton;


    @FXML
    private void exit() {
        applicationService.stopApplication();
    }

    @FXML
    private void closeWindow() {
        applicationService.closeSecondWindow();
    }

    @FXML
    private void showDocumentation() {
        applicationService.createNewWindow("about",
                new AboutController(applicationService), "About", 720, 520);
    }

    @FXML
    private void switchToLoginWindow() {
        applicationService.changeRootStage("loginForSE",
                new LoginForSEController(applicationService));
    }

    @FXML
    private void switchToPrimary() {
        applicationService.changeRootStage("primary", new PrimaryController(applicationService));
    }

    public Controller(ApplicationService applicationService) {
        this.applicationService = applicationService;
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
