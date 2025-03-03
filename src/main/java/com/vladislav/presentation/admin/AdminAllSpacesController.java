package com.vladislav.presentation.admin;

import com.vladislav.presentation.WindowService;
import com.vladislav.presentation.EventTablesController;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.domain.Space;
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
import org.springframework.beans.factory.annotation.Autowired;

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

    public AdminAllSpacesController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    public void switchToPrimary() {
        windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginWindowButton.setVisible(false);
        ArrayList<String> spaceTypeList = new ArrayList<>() {{
            add("Все");
            add("Для мероприятий");
            add("Для кружков");
        }};
        filter.setValue("Все");
        filter.setItems(FXCollections.observableList(spaceTypeList));
        filter.valueProperty().addListener((observable, oldValue, newValue) -> filteredSpaceList.setPredicate(space -> {
            boolean result;
            String type = space.getType();
            if (newValue == null || newValue.isEmpty() || newValue.equals("Все")) result = true;
            else {
                result = switch (newValue) {
                    case "Для мероприятий" -> type.equals("event");
                    case "Для кружков" -> type.equals("lesson");
                    default -> false;
                };
            }
            tableOfSpaces.refresh();
            return result;
        }));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        typeColumn.setCellValueFactory(cell ->
                switch (cell.getValue().getType()) {
                    case "event" -> new SimpleStringProperty("Для событий");
                    case "lesson" -> new SimpleStringProperty("Для кружков");
                    default -> new SimpleStringProperty("error");
        });


        DataBase.loadSpacesList(null);
        filteredSpaceList = new FilteredList<>(FXCollections.observableArrayList(Space.objectsList));
        tableOfSpaces.setItems(filteredSpaceList);
    }
}
