package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.controllers.EditTasksTypeController;
import com.vladislav.models.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminCreateNewTaskController extends Controller implements Initializable {
    FilteredList<Event> filteredEventsList;
    FilteredList<TaskType> taskTypesList;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, StringProperty> eventColumn;

    @FXML
    private TableColumn<Event, StringProperty> startTimeColumn;

    @FXML
    private TableColumn<Event, String> eventTypeColumn;

    @FXML
    private Text warningEvent;

    @FXML
    private RadioButton executed;

    @FXML
    private RadioButton noExecuted;

    @FXML
    private TableView<TaskType> typeTable;

    @FXML
    private TableColumn<TaskType, StringProperty> taskType;

    @FXML
    private TableColumn<TaskType, StringProperty> typeDescriptionColumn;

    @FXML
    private Text warningType;

    @FXML
    private ComboBox<String> minutes;

    @FXML
    private RadioButton minutesBefore;

    @FXML
    private RadioButton toStartEvent;

    @FXML
    private Text successfulSaving;

    @FXML
    void cleanForm() {
        hideWarnings();
        executed.setSelected(false);
        noExecuted.setSelected(true);
        minutesBefore.setSelected(false);
        toStartEvent.setSelected(true);
        minutes.setValue(null);
        eventTable.getSelectionModel().clearSelection();
        typeTable.getSelectionModel().clearSelection();
    }

    @FXML
    void save() {
        TableView.TableViewSelectionModel<Event> eventSelectionModel = eventTable.getSelectionModel();
        ObservableList<Event> eventList = eventSelectionModel.getSelectedItems();
        Event event = null;

        boolean flag = true;
        if (eventList.size() != 0) event = eventList.get(0);
        else {
            warningEvent.setVisible(true);
            flag = false;
        }

        TableView.TableViewSelectionModel<TaskType> typeSelectionModel = typeTable.getSelectionModel();
        ObservableList<TaskType> typeList = typeSelectionModel.getSelectedItems();
        TaskType type = null;
        if (typeList.size() != 0) type = typeList.get(0);
        else {
            warningType.setVisible(true);
            flag = false;
        }

        Status status;
        if (executed.isSelected()) status = Status.EXECUTED;
        else status = Status.CREATED;

        if (!flag) return;

        Long deadline;
        if (toStartEvent.isSelected() && event != null) deadline = event.getTimeToStart();
        else {
            String delay = minutes.getValue();
            if (delay != null && !delay.isEmpty() && event != null) {
                deadline = Long.parseLong(delay) * 60000L + event.getTimeToStart();
            } else return;
        }
        DataBase.addTask(System.currentTimeMillis(), type, event, deadline, status);
        cleanForm();
    }

    @FXML
    void checkExecutedStatus() {
        hideWarnings();
        noExecuted.setSelected(false);
        if (!executed.isSelected()) executed.setSelected(true);
    }

    @FXML
    void checkNoExecutedStatus() {
        hideWarnings();
        executed.setSelected(false);
        if (!noExecuted.isSelected()) noExecuted.setSelected(true);
    }

    @FXML
    void checkDeadlineStart() {
        hideWarnings();
        minutesBefore.setSelected(false);
        if (!toStartEvent.isSelected()) toStartEvent.setSelected(true);
        else minutes.setDisable(true);
    }

    @FXML
    void checkDeadlineBefore() {
        hideWarnings();
        toStartEvent.setSelected(false);
        if (!minutesBefore.isSelected()) minutesBefore.setSelected(true);
        else minutes.setDisable(toStartEvent.isSelected());
    }

    @FXML
    void hideWarnings() {
        warningEvent.setVisible(false);
        warningType.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    private void switchToPrimary() {
        App.setRoot("adminDesktop", new AdminDesktopController());
}

    @FXML
    void switchToEditEvents() {
        App.setRoot("createNewEvent", new AdminCreateNewEventController());
    }

    @FXML
    void switchToEditTypes() {
        App.newWindow("editTaskType", new EditTasksTypeController(), "Редактирование видов заявок",
                505, 490);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // eventTable
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        eventTypeColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
        DataBase.loadEvents(null);
        FilteredList<Event> filteredEventsList = new FilteredList<>(Event.objectsList, t -> true);
        eventTable.setItems(filteredEventsList);

        // typeTable
        taskType.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        DataBase.loadTypeTaskList();
        FilteredList<TaskType> taskTypesList = new FilteredList<>(TaskType.objectsList, p -> true);
        typeTable.setItems(taskTypesList);
    }
}
