package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.models.DataBase;
import com.vladislav.models.Lesson;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminLessonsController extends Controller implements Initializable {

    @FXML
    private Label title;

    @FXML
    private TableView<Lesson> tableOfLessons;

    @FXML
    private TableColumn<Lesson, String> titleColumn;

    @FXML
    private TableColumn<Lesson, String> mondayColumn;

    @FXML
    private TableColumn<Lesson, String> tuesdayColumn;

    @FXML
    private TableColumn<Lesson, String> wednesdayColumn;

    @FXML
    private TableColumn<Lesson, String> thursdayColumn;

    @FXML
    private TableColumn<Lesson, String> fridayColumn;

    @FXML
    private TableColumn<Lesson, String> saturdayColumn;

    @FXML
    private TableColumn<Lesson, String> sundayColumn;

    @FXML
    public void switchToPrimary() {
//        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginWindowButton.setVisible(false);
        title.setText("Расписание кружков");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        mondayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(0));
        tuesdayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(1));
        wednesdayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(2));
        thursdayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(3));
        fridayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(4));
        saturdayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(5));
        sundayColumn.setCellValueFactory(cell -> cell.getValue().getScheduleDay(6));

        DataBase.loadLesson();
        FilteredList<Lesson> filteredList = new FilteredList<>(Lesson.objectsList, p -> true);
        tableOfLessons.setItems(filteredList);
    }
}
