package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.DataBase;
import com.vladislav.models.Task;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminAllTasksController extends EventTablesController implements Initializable {

    @FXML
    private TableColumn<Task, StringProperty> deadlineColumn;

    @FXML
    private TableColumn<Task, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Task, StringProperty> eventColumn;

    @FXML
    private TableColumn<Task, StringProperty> spaceColumn;

    @FXML
    private TableColumn<Task, StringProperty> statusColumn;

    @FXML
    private TableView<Task> tableOfTasks;

    @FXML
    private TableColumn<Task, StringProperty> timeRegColumn;

    @FXML
    private TableColumn<Task, StringProperty> typeColumn;

    @FXML
    @Override
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableOfTasks.setRowFactory(row -> new TableRow<Task>(){
        @Override
        public void updateItem(Task item, boolean empty) {
            if (!empty) {
                switch (item.getStatus()) {
                    case CREATED:
                        setStyle("-fx-background-color: null"); break;
                    case EXECUTED:
                        setStyle("-fx-background-color: pink"); break;
                    case COMPLETED:
                        setStyle("-fx-background-color: grey"); break;
                    case OVERDUE:
                        setStyle("-fx-background-color: red"); break;
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
                    String info = this.getChildren().get(6).toString();
                    int startIndex = info.indexOf("'") + 1;
                    int endIndex = info.length() - 1;
                    String taskType = info.substring(startIndex, endIndex);
                    switch (taskType) {
                        case "Создана":
                            node.setStyle("-fx-background-color: null; -fx-fill: black");
                            break;
                        case "К выполнению":
                            node.setStyle("-fx-background-color: pink; -fx-fill: black");
                            break;
                        case "Выполнена":
                            node.setStyle("-fx-background-color: grey; -fx-fill: black");
                            break;
                        case "Просрочена":
                            node.setStyle("-fx-background-color: null; -fx-fill: red");
                            break;
                    }
                });
            }
            super.updateSelected(var1);
        }
    });
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineString"));
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        spaceColumn.setCellValueFactory(new PropertyValueFactory<>("spaceName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusName"));
        timeRegColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfRegName"));


        ArrayList<Task> events = DataBase.getTasksList(false);
        if (!events.isEmpty()) {
            ObservableList<Task> listOfStrings = FXCollections.observableArrayList(events);
            tableOfTasks.setItems(listOfStrings);
        }
    }
}
