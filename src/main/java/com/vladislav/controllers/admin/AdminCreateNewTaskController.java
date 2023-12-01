package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.models.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminCreateNewTaskController extends Controller implements Initializable {

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, StringProperty> eventColumn;

    @FXML
    private TableColumn<Event, StringProperty> startTimeColumn;

    @FXML
    private TableColumn<Event, String> eventTypeColumn;

    @FXML
    private TableView<TaskType> typeTable;

    @FXML
    private TableColumn<TaskType, StringProperty> taskType;

    @FXML
    private TableColumn<TaskType, StringProperty> typeDescriptionColumn;

    @FXML
    private ComboBox<String> minutes;

    @FXML
    private RadioButton executed;

    @FXML
    private RadioButton noExecuted;

    @FXML
    private RadioButton minutesBefore;

    @FXML
    private RadioButton toStartEvent;

    @FXML
    void cleanForm() {
        executed.setSelected(false);
        noExecuted.setSelected(true);
        minutesBefore.setSelected(false);
        toStartEvent.setSelected(false);
        minutes.setValue(null);
        eventTable.getSelectionModel().clearSelection();
        typeTable.getSelectionModel().clearSelection();
    }

    @FXML
    void save() {
        TableView.TableViewSelectionModel<Event> eventSelectionModel = eventTable.getSelectionModel();
        ObservableList<Event> eventList = eventSelectionModel.getSelectedItems();
        Event event;
        if (eventList.size() != 0) event = eventList.get(0);
        else {
            App.logger.error("Не выбранно Мероприятие");
            return;
        }

        TableView.TableViewSelectionModel<TaskType> typeSelectionModel = typeTable.getSelectionModel();
        ObservableList<TaskType> typeList = typeSelectionModel.getSelectedItems();
        TaskType type;
        if (typeList.size() != 0) type = typeList.get(0);
        else {
            App.logger.error("Не выбран тип");
            return;
        }

        Status status;
        if (executed.isSelected()) status = Status.EXECUTED;
        else status = Status.CREATED;

        Long deadline;
        if (toStartEvent.isSelected()) deadline = event.getTimeToStart();
        else {
            String delay = minutes.getValue();
            if (delay != null && !delay.isEmpty()) {
                deadline = Long.parseLong(delay) * 60000L;
            } else {
                App.logger.error("Не выставенные минуты до Мероприятия");
                return;
            }
        }
        DataBase.addTask(System.currentTimeMillis(), type, event, deadline, status);
        cleanForm();
    }

    @FXML
    void checkExecutedStatus() {
        noExecuted.setSelected(false);
    }

    @FXML
    void checkNoExecutedStatus() {
        executed.setSelected(false);
    }

    @FXML
    void checkDeadlineStart() {
        minutesBefore.setSelected(false);
        minutes.setDisable(true);
    }

    @FXML
    void checkDeadlineBefore() {
        toStartEvent.setSelected(false);
        minutes.setDisable(toStartEvent.isSelected());
    }

    @FXML
    void hideWarnings() {

    }

    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop", new AdminDesktopController());
}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // eventTable
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        eventTypeColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        // typeTable
        taskType.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        ArrayList<Event> eventList = DataBase.getEventsList(null);
        if (!eventList.isEmpty()) {
            ObservableList<Event> eventsRows = FXCollections.observableArrayList(eventList);
            eventTable.setItems(eventsRows);
        }

        ArrayList<TaskType> taskTypesList = DataBase.getTypeTaskList();
        if (!eventList.isEmpty()) {
            ObservableList<TaskType> taskTypesRows = FXCollections.observableArrayList(taskTypesList);
            typeTable.setItems(taskTypesRows);
        }
    }
}
