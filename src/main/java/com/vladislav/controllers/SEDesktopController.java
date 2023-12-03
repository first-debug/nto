package com.vladislav.controllers;

import com.vladislav.models.*;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SEDesktopController extends Controller implements Initializable {

    private FilteredList<Task> filteredList;
    @FXML
    private ChoiceBox<String> filter;
    @FXML
    private TableView<Task> tableOfTasks;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> eventColumn;
    @FXML
    private TableColumn<Task, String> spaceColumn;
    @FXML
    private TableColumn<Task, StringProperty> deadlineColumn;
    @FXML
    private TableColumn<Task, StringProperty> descriptionColumn;

    @FXML
    private void markCompleted() {
        TableView.TableViewSelectionModel<Task> selectionModel = tableOfTasks.getSelectionModel();
        ObservableList<Task> tasksList = selectionModel.getSelectedItems();
        tasksList.forEach(task -> {
            DataBase.changeTaskStatus(task, Status.COMPLETED);
            task.setCompleted();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<TaskType> spacesList = DataBase.getTypeTaskList();
        ArrayList<String> spaceStringsList = new ArrayList<>();
        spaceStringsList.add("Все");
        for (TaskType sp : spacesList) spaceStringsList.add(sp.getName());
        if (!spacesList.isEmpty()) {
            filter.setItems(FXCollections.observableList(spaceStringsList));
        }
        filter.setValue("Все");
        filter.valueProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(task -> {
            boolean result;
            if (newValue == null || newValue.isEmpty() || newValue.equals("Все")) result = true;
            else result = task.getType().getName().equals(newValue);
            tableOfTasks.refresh();
            return result;
        }));


        nameColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        statusColumn.setCellValueFactory(cell -> cell.getValue().getStatus().nameProperty());
        eventColumn.setCellValueFactory(cell -> cell.getValue().getEvent().titleProperty());
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineString"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableOfTasks.setRowFactory(row -> new TableRow<>() {
            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (item.getStatus()) {
                        case EXECUTED:
                            setStyle("-fx-background-color: pink");
                            break;
                        case COMPLETED:
                            setStyle("-fx-background-color: grey");
                            break;
                    }
                }
            }

            @Override
            public void updateSelected(boolean var1) {
                if (var1) {
                    getChildren().forEach(node -> node.setStyle("-fx-background-color: #0095c7;-fx-fill: white"));
                } else {
                    getChildren().forEach(node -> {
                        String info = this.getChildren().get(1).toString();
                        int startIndex = info.indexOf("'") + 1;
                        int endIndex = info.length() - 1;
                        String taskType = info.substring(startIndex, endIndex);
                        switch (taskType) {
                            case "К выполнению":
                                node.setStyle("-fx-background-color: pink; -fx-fill: black");
                                break;
                            case "Выполнена":
                                node.setStyle("-fx-background-color: grey; -fx-fill: black");
                                break;
                        }
                    });
                }
            }
        });

        ArrayList<Task> tasksList = DataBase.getTasksList(true, null);
        if (!tasksList.isEmpty()) {
            ObservableList<Task> list = FXCollections.observableList(tasksList);
            filteredList = new FilteredList<>(list, t -> true);
            tableOfTasks.setItems(filteredList);
        }
    }
}
