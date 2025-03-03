package com.vladislav.presentation.admin;

import com.vladislav.domain.Event;
import com.vladislav.domain.Status;
import com.vladislav.domain.Task;
import com.vladislav.domain.TaskType;
import com.vladislav.presentation.WindowService;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.presentation.Controller;
import com.vladislav.presentation.EditTasksTypeController;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminCreateNewTaskController extends Controller implements Initializable {
    FilteredList<Event> filteredEventsList;
    FilteredList<TaskType> taskTypesList;

    @FXML
    private TableColumn<Task, StringProperty> deadlineColumn;
    @FXML
    private TableColumn<Task, StringProperty> descriptionColumn;
    @FXML
    private TableColumn<Task, String> eventNameColumn;
    @FXML
    private TableColumn<Task, String> spaceColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableView<Task> tableOfTasks;
    @FXML
    private TableColumn<Task, StringProperty> timeRegColumn;
    @FXML
    private TableColumn<Task, String> typeColumn;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, StringProperty> eventColumn;
    @FXML
    private TableColumn<Event, StringProperty> eventDescriptionColumn;
    @FXML
    private TableColumn<Event, String> spaceEventColumn;
    @FXML
    private TableColumn<Event, StringProperty> startColumn;
    @FXML
    private TableColumn<Event, String> eventTypeColumn;
    @FXML
    private Text warningEvent;
    @FXML
    private Text warningTasks;
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
    private TabPane tabPane;
    @FXML
    private Tab taskTab;

    public AdminCreateNewTaskController(@Autowired WindowService windowService) {
        super(windowService);
    }

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
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Task> selectionModel = tableOfTasks.getSelectionModel();
        ObservableList<Task> spaceList = selectionModel.getSelectedItems();

        if (spaceList == null || spaceList.isEmpty()) {
            warningTasks.setText("Нужно выбрать хотя бы одно событие!");
            warningTasks.setVisible(true);
            return;
        }
        spaceList.forEach(DataBase::removeTask);
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<Task> selectionModel = tableOfTasks.getSelectionModel();
        ObservableList<Task> selectedSpaces = selectionModel.getSelectedItems();
        if (selectedSpaces == null || selectedSpaces.isEmpty()) {
            warningTasks.setText("Нужно выбрать только одну заявку!");
            warningTasks.setVisible(true);
            return;
        }
        if (selectedSpaces.size() == 1) {
            Task task = selectedSpaces.get(0);
            typeTable.getSelectionModel().select(task.getType());
            eventTable.getSelectionModel().select(task.getEvent());
            tabPane.getSelectionModel().select(taskTab);
            executed.setSelected(task.getStatus().equals(Status.EXECUTED));
            noExecuted.setSelected(!task.getStatus().equals(Status.EXECUTED));
            toStartEvent.setSelected(task.getDeadline().equals(task.getEvent().getTimeToStart()));
            minutesBefore.setSelected(!task.getDeadline().equals(task.getEvent().getTimeToStart()));
            if (minutesBefore.isSelected()) {
                minutes.setDisable(false);
                minutes.setValue(String.valueOf((task.getEvent().getTimeToStart() - task.getDeadline()) / 60000));
            }
        } else {
            warningTasks.setText("Нужно выбрать только одну заявку!");
            warningTasks.setVisible(true);
        }

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
        warningType.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    private void switchToPrimary() {
        windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
    }

    @FXML
    void switchToEditEvents() {
        windowService.changeRootStage("createNewEvent", new AdminCreateNewEventController(windowService));
    }

    @FXML
    void switchToEditTypes() {
        windowService.createNewWindow("editType", new EditTasksTypeController(windowService), "Редактирование видов заявок",
                505, 490);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // eventTable
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        spaceEventColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        eventTypeColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());


        DataBase.loadEvents(null);
        FilteredList<Event> filteredEventsList = new FilteredList<>(Event.objectsList, t -> true);
        eventTable.setItems(filteredEventsList);

        // typeTable
        taskType.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        DataBase.loadTaskTypeList();
        FilteredList<TaskType> taskTypesList = new FilteredList<>(TaskType.objectsList, p -> true);
        typeTable.setItems(taskTypesList);


        tableOfTasks.setRowFactory(row -> new TableRow<>() {
            @Override
            public void updateItem(Task item, boolean empty) {
//                if (!empty) {
//                    switch (item.getStatus()) {
//                        case CREATED:
//                            setStyle("-fx-background-color: null; " +
//                                    "-fx-border-color: transparent -fx-table-cell-border-color " +
//                                    "-fx-table-cell-border-color transparent; ");
//                            break;
//                        case EXECUTED:
//                            setStyle("-fx-background-color: pink; " +
//                                    "-fx-border-color: transparent -fx-table-cell-border-color " +
//                                    "-fx-table-cell-border-color transparent; ");
//                            break;
//                        case COMPLETED:
//                            setStyle("-fx-background-color: grey; " +
//                                    "-fx-border-color: transparent -fx-table-cell-border-color " +
//                                    "-fx-table-cell-border-color transparent; ");
//                            break;
//                        case OVERDUE:
//                            setStyle("-fx-background-color: red; " +
//                                    "-fx-border-color: transparent -fx-table-cell-border-color " +
//                                    "-fx-table-cell-border-color transparent;");
//                            break;
//                    }
//                    super.updateItem(item, false);
//                }
                tableOfTasks.getSelectionModel().select(item);
                tableOfTasks.getSelectionModel().clearSelection();
            }

            @Override
            public void updateSelected(boolean var1) {
                if (var1) {
                    this.getChildren().forEach(node -> node.setStyle("-fx-background-color: #0095c7;-fx-fill: white"));
                } else {
                    this.getChildren().forEach(node -> {
                        String info = this.getChildren().get(6).toString();
                        int startIndex = info.indexOf("'") + 1;
                        int endIndex = info.length() - 1;
                        String taskType = info.substring(startIndex, endIndex);
                        switch (taskType) {
                            case "Создана":
                                node.setStyle("-fx-background-color: null; " +
                                        "-fx-fill: black; " +
                                        "-fx-border-color: transparent -fx-table-cell-border-color " +
                                        "-fx-table-cell-border-color transparent;");
                                break;
                            case "К выполнению":
                                node.setStyle("-fx-background-color: pink; " +
                                        "-fx-fill: black; " +
                                        "-fx-border-color: transparent -fx-table-cell-border-color " +
                                        "-fx-table-cell-border-color transparent;");
                                break;
                            case "Выполнена":
                                node.setStyle("-fx-background-color: grey; " +
                                        "-fx-fill: black; " +
                                        "-fx-border-color: transparent -fx-table-cell-border-color " +
                                        "-fx-table-cell-border-color transparent;");
                                break;
                            case "Просрочена":
                                node.setStyle("-fx-background-color: null; " +
                                        "-fx-fill: red; " +
                                        "-fx-border-color: transparent -fx-table-cell-border-color " +
                                        "-fx-table-cell-border-color transparentransparent;");
                                break;
                        }
                    });
                }
                super.updateSelected(var1);
            }
        });
        typeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineString"));
        eventNameColumn.setCellValueFactory(cell -> cell.getValue().getEvent().titleProperty());
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().getStatus().nameProperty());
        timeRegColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfRegName"));


        DataBase.loadTasksList(false, null);
        FilteredList<Task> filteredTaskList = new FilteredList<>(Task.objectsList, p -> true);
        tableOfTasks.setItems(filteredTaskList);
    }
}
