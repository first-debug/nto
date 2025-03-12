package com.vladislav.presentation;

import com.vladislav.presentation.primary.PrimaryController;
import com.vladislav.presentation.services.WindowService;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;

@Component
public class Controller {

    protected final WindowService windowService;

    @FXML
    protected MenuItem loginWindowButton;


    @FXML
    private void exit() {
        windowService.stopApplication();
    }

    @FXML
    private void closeWindow() {
        windowService.closeSecondWindow();
    }

    @FXML
    private void showDocumentation() {
        windowService.createNewWindow("about",
                new AboutController(windowService), "About", 720, 520);
    }

    @FXML
    private void switchToLoginWindow() {
        windowService.changeRootStage("loginForSE",
                new LoginForSEController(windowService));
    }

    @FXML
    private void switchToPrimary() {
        windowService.changeRootStage("primary", new PrimaryController(windowService));
    }

    public Controller(WindowService windowService) {
        this.windowService = windowService;
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
