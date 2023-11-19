package com.vladislav.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.vladislav.models.Event;
import com.vladislav.App;
import com.vladislav.models.DataBase;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.LongProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EntertainmentsController extends Controller implements Initializable {

    @FXML
    private TableView<Event> tableOfEvents;

    @FXML
    private TableColumn<Event, StringProperty> nameColumn;

    @FXML
    private TableColumn<Event, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Event, LongProperty> startClolumn;

    @FXML
    private TableColumn<Event, StringProperty> typeColumn;
    
    @FXML
    private Button goBackButton;

    @FXML
    private void switchToPrimary(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Event, StringProperty>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Event, StringProperty>("description"));
        startClolumn.setCellValueFactory(new PropertyValueFactory<Event, LongProperty>("timeToStart"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Event, StringProperty>("type"));

        List<Event> entEvents = DataBase.getEntEvents();
        if (!entEvents.isEmpty()) {
                ObservableList<Event> listOfStrings = FXCollections.observableArrayList(entEvents);
                tableOfEvents.setItems(listOfStrings);
            }
    }
}