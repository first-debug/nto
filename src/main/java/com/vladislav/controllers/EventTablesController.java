package com.vladislav.controllers;

import com.vladislav.models.DataBase;
import com.vladislav.models.Event;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class EventTablesController extends Controller {

    @FXML
    private TableView<Event> tableOfEvents;

    @FXML
    private TableColumn<Event, StringProperty> nameColumn;

    @FXML
    private TableColumn<Event, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Event, StringProperty> startColumn;

    @FXML
    private TableColumn<Event, StringProperty> typeColumn;

    public void createEventsTable(boolean isEntertainment) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        ArrayList<Event> events;
        if (isEntertainment)  events = DataBase.getEntEvents();
        else events = DataBase.getEduEvents();

        if (!events.isEmpty()) {
            ObservableList<Event> listOfStrings = FXCollections.observableArrayList(events);
            tableOfEvents.setItems(listOfStrings);
        }
    }

    public void createEventsTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        ArrayList<Event> events = DataBase.getEventsList(null);
        if (!events.isEmpty()) {
            ObservableList<Event> listOfStrings = FXCollections.observableArrayList(events);
            tableOfEvents.setItems(listOfStrings);
        }
    }
//    tableOfTasks.setRowFactory(row -> new TableRow<>(){
//        @Override
//        public void updateItem(Task item, boolean empty) {
//            if (!empty) {
//                switch (item.getStatus()) {
//                    case CREATED:
//                        setStyle("-fx-background-color: null"); break;
//                    case EXECUTED:
//                        setStyle("-fx-background-color: pink"); break;
//                    case COMPLETED:
//                        setStyle("-fx-background-color: grey"); break;
//                    case OVERDUE:
//                        setStyle("-fx-background-color: red"); break;
//                }
//                super.updateItem(item, false);
//            }
//        }
//
//        @Override
//        public void updateSelected(boolean var1) {
//            if (var1) {
//                this.getChildren().forEach(node -> node.setStyle("-fx-background-color: #0095c7;-fx-fill: white"));
//            } else {
//                this.getChildren().forEach(node -> {
//                    String info = this.getChildren().get(1).toString();
//                    int startIndex = info.indexOf("'") + 1;
//                    int endIndex = info.length() - 1;
//                    String taskType = info.substring(startIndex, endIndex);
//                    switch (taskType) {
//                        case "Создана":
//                            node.setStyle("-fx-background-color: null; -fx-fill: black");
//                            break;
//                        case "К выполнению":
//                            node.setStyle("-fx-background-color: pink; -fx-fill: black");
//                            break;
//                        case "Выполнена":
//                            node.setStyle("-fx-background-color: grey; -fx-fill: black");
//                            break;
//                        case "Просрочена":
//                            node.setStyle("-fx-background-color: null; -fx-fill: red");
//                            break;
//                    }
//                });
//            }
//            super.updateSelected(var1);
//        }
//    });
}
