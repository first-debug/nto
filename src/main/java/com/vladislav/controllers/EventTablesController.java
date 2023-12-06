package com.vladislav.controllers;

import com.vladislav.models.DataBase;
import com.vladislav.models.Event;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.function.Predicate;

public class EventTablesController extends Controller {

    @FXML
    private Label title;

    @FXML
    private TableView<Event> tableOfEvents;

    @FXML
    private TableColumn<Event, StringProperty> nameColumn;

    @FXML
    private TableColumn<Event, String> descriptionColumn;

    @FXML
    private TableColumn<Event, String> spaceColumn;

    @FXML
    private TableColumn<Event, StringProperty> startColumn;

    @FXML
    private TableColumn<Event, String> typeColumn;

    public void createEventsTable(Boolean isEntertainment, String title) {
        this.title.setText(title);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        DataBase.loadEvents(isEntertainment);
        Predicate<Event> predicate = event -> {
            if (isEntertainment == null) return true;
            return event.getIsEntertainment() == isEntertainment;
        };
        FilteredList<Event> filteredEventList = new FilteredList<>(Event.objectsList, predicate);
        tableOfEvents.setItems(filteredEventList);
    }
}
