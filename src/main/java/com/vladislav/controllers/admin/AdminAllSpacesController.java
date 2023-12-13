package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.EventTablesController;
import com.vladislav.models.DataBase;
import com.vladislav.models.Space;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminAllSpacesController extends EventTablesController implements Initializable {

    private FilteredList<Space> filteredSpaceList;
    @FXML
    private ChoiceBox<String> filter;
    @FXML
    private TableView<Space> tableOfSpaces;
    @FXML
    private TableColumn<Space, StringProperty> nameColumn;
    @FXML
    private TableColumn<Space, StringProperty> descriptionColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;
    @FXML
    private TableColumn<Space, String> typeColumn;

    @FXML
    public void switchToPrimary() throws IOException {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> spaceTypeList = new ArrayList<String>() {{
            add("Все");
            add("Для мероприятий");
            add("Для кружков");
        }};
        filter.setValue("Все");
        filter.setItems(FXCollections.observableList(spaceTypeList));
        filter.valueProperty().addListener((observable, oldValue, newValue) -> filteredSpaceList.setPredicate(space -> {
            boolean result = false;
            String type = space.getType();
            if (newValue == null || newValue.isEmpty() || newValue.equals("Все")) result = true;
            else {
                switch (newValue) {
                    case "Для мероприятий":
                        result = type.equals("event");
                        break;
                    case "Для кружков":
                        result = type.equals("lesson");
                        break;
                }
            }
            tableOfSpaces.refresh();
            return result;
        }));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        typeColumn.setCellValueFactory(cell -> {
            switch (cell.getValue().getType()) {
                case "event":
                    return new SimpleStringProperty("Для событий");
                case "lesson":
                    return new SimpleStringProperty("Для кружков");
            }
            return new SimpleStringProperty("error");
        });


        DataBase.loadSpacesList(null);
        filteredSpaceList = new FilteredList<>(FXCollections.observableArrayList(Space.objectsList));
        tableOfSpaces.setItems(filteredSpaceList);
    }
}
