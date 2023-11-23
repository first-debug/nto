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

public class TablesController extends Controller {

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
        ArrayList<Event> events;
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

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
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        ArrayList<Event> events = DataBase.getListEvents(null);
        if (!events.isEmpty()) {
            ObservableList<Event> listOfStrings = FXCollections.observableArrayList(events);
            tableOfEvents.setItems(listOfStrings);
        }
    }
}
