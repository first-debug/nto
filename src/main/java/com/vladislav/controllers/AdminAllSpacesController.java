package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.DataBase;
import com.vladislav.models.Space;
import com.vladislav.models.Task;
import javafx.beans.property.IntegerProperty;
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

public class AdminAllSpacesController extends EventTablesController implements Initializable {

    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;

    @FXML
    private TableColumn<Space, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;

    @FXML
    private TableView<Space> tableOfSpaces;

    @FXML
    private TableColumn<Space, StringProperty> nameColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> idColumn;

    @FXML
    @Override
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));


        ArrayList<Space> events = DataBase.getSpacesList();
        if (!events.isEmpty()) {
            ObservableList<Space> listOfStrings = FXCollections.observableArrayList(events);
            tableOfSpaces.setItems(listOfStrings);
        }
    }
}
