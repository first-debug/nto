package com.vladislav.controllers;

import com.vladislav.models.*;
import javafx.beans.property.Property;
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

public class DesktopForSEController extends Controller implements Initializable {


    FilteredList<Task> filteredList;

    @FXML
    private ChoiceBox<String> filter;

    @FXML
    private TableView<Task> tableOfTasks;

    @FXML
    private TableColumn<Task, StringProperty> nameColumn;

    @FXML
    private TableColumn<Task, Status> statusColumn;

    @FXML
    private TableColumn<Task, Property<Event>> eventColumn;

    @FXML
    private TableColumn<Task, StringProperty> spaceColumn;

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
        ArrayList<TaskType> spacesList = DataBase.getTaskTypeList();
        ArrayList<String> spaceStringsList = new ArrayList<>();
        spaceStringsList.add("Все");
        for (TaskType sp : spacesList) spaceStringsList.add(sp.getName());
        if (!spacesList.isEmpty()) {
            filter.setItems(FXCollections.observableList(spaceStringsList));
        }
        filter.setValue("Все");
        tableOfTasks.setRowFactory(row -> new TableRow<Task>(){
            @Override
            public void updateItem(Task item, boolean empty) {
                if (!empty) {
                    switch (item.getStatus()) {
                        case EXECUTED:
                            setStyle("-fx-background-color: pink"); break;
                        case COMPLETED:
                            setStyle("-fx-background-color: grey"); break;
                    }
                    super.updateItem(item, false);
                }
            }

            @Override
            public void updateSelected(boolean var1) {
                if (var1) {
                    this.getChildren().forEach(node -> node.setStyle("-fx-background-color: #0095c7;-fx-fill: white"));
                } else {
                    this.getChildren().forEach(node -> {
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
                super.updateSelected(var1);
            }
        });
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusName"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        spaceColumn.setCellValueFactory(new PropertyValueFactory<>("spaceName"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineString"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        ArrayList<Task> tasksList = DataBase.getTasksList(true);
        if (!tasksList.isEmpty()) {
            ObservableList<Task> list = FXCollections.observableList(tasksList);
            filteredList = new FilteredList<>(list, l -> true);
            filter.valueProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(task -> {
                if (newValue.equals("Все")) return true;
                return task.getTypeName().equals(newValue);
            }));
            tableOfTasks.setItems(filteredList);
        }

    }
}
