package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.EventTablesController;
import com.vladislav.models.DataBase;
import com.vladislav.models.Space;
import com.vladislav.models.Task;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private TableView<Space> tableOfSpaces;
    @FXML
    private TableColumn<Space, IntegerProperty> idColumn;
    @FXML
    private TableColumn<Space, StringProperty> nameColumn;
    @FXML
    private TableColumn<Space, StringProperty> descriptionColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;

    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));


        DataBase.getSpacesList();
        FilteredList<Space> filteredSpaceList = new FilteredList<>(FXCollections.observableArrayList(Space.objectsList));
        tableOfSpaces.setItems(filteredSpaceList);
    }
}
