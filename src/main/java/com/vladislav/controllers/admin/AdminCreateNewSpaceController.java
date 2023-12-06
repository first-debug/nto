package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.models.DataBase;
import com.vladislav.models.Space;
import com.vladislav.models.TaskType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminCreateNewSpaceController extends Controller implements Initializable {

    @FXML
    private TextField titleInput;

    @FXML
    private Text warningTitle;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private Text warningDescription;

    @FXML
    private TextField areaInput;

    @FXML
    private TextField capacityInput;

    @FXML
    private RadioButton oneOrTwo;

    @FXML
    private RadioButton onlyOneEvent;

    @FXML
    private Text areaText1;

    @FXML
    private TextField firstArea;

    @FXML
    private Text areaText2;

    @FXML
    private Text areaText3;

    @FXML
    private TextField secondArea;

    @FXML
    private Text areaText4;

    @FXML
    private TableView<Space> spacesTable;

    @FXML
    private TableColumn<Space, StringProperty> spaceColumn;

    @FXML
    private TableColumn<Space, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;

    @FXML
    private Text warningSpace;

    @FXML
    private Text successfulSaving;

    void showPartAreaInputs(boolean value) {
        String style = "-fx-fill:" + (value ? "black" : "grey");
        areaText1.setStyle(style);
        firstArea.setDisable(!value);
        areaText2.setStyle(style);
        areaText3.setStyle(style);
        secondArea.setDisable(!value);
        areaText4.setStyle(style);
    }

    @FXML
    void checkOnlyOneEvent() {
        hideWarnings();
        oneOrTwo.setSelected(false);
        showPartAreaInputs(false);
        if (!onlyOneEvent.isSelected()) onlyOneEvent.setSelected(true);
    }

    @FXML
    void checkOneOrTwo() {
        hideWarnings();
        onlyOneEvent.setSelected(false);
        showPartAreaInputs(true);
        if (!oneOrTwo.isSelected()) oneOrTwo.setSelected(true);
    }

    @FXML
    void cleanForm() {
        titleInput.setText(null);
        descriptionInput.setText(null);
        areaInput.setText(null);
        capacityInput.setText(null);
        onlyOneEvent.setSelected(true);
        showPartAreaInputs(false);
        oneOrTwo.setSelected(false);
        spacesTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Space> selectionModel = spacesTable.getSelectionModel();
        ObservableList<Space> spaceList = selectionModel.getSelectedItems();

        if (spaceList == null || spaceList.isEmpty()) {
            warningSpace.setVisible(true);
            return;
        }
        spaceList.forEach(DataBase::removeSpace);
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<Space> selectionModel = spacesTable.getSelectionModel();
        ObservableList<Space> selectedSpaces = selectionModel.getSelectedItems();
        if (selectedSpaces == null || selectedSpaces.isEmpty()) {
            warningSpace.setVisible(true);
            return;
        }
        if (selectedSpaces.size() == 1) {
            Space space = selectedSpaces.get(0);
            titleInput.setText(space.getName());
            descriptionInput.setText(space.getDescription());
            areaInput.setText(space.getArea().toString());
            capacityInput.setText(space.getCapacity().toString());
            Integer[] partsArea = space.getPartsArea();
            if (partsArea.length != 0) {
                onlyOneEvent.setSelected(false);
                showPartAreaInputs(true);
                oneOrTwo.setSelected(true);
                firstArea.setText(partsArea[0].toString());
                secondArea.setText(partsArea[1].toString());
            } else {
                onlyOneEvent.setSelected(true);
                showPartAreaInputs(false);
                oneOrTwo.setSelected(false);
            }
        }
    }

    @FXML
    void hideWarnings() {
        warningSpace.setVisible(false);
        warningDescription.setVisible(false);
        warningTitle.setVisible(false);
    }

    @FXML
    void save() {
        hideWarnings();
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String areaStr = areaInput.getText();
        String capStr = capacityInput.getText();
        Boolean isOnlyOne = onlyOneEvent.isSelected();

        boolean flag = true;
        if (title == null || title.isEmpty())
        {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (description == null || description.isEmpty())
        {
            warningDescription.setVisible(true);
            flag = false;
        }
        if (!flag) return;

        DataBase.addSpace(title, description, areaStr.isEmpty() ? -1 : Integer.parseInt(areaStr),
                capStr.isEmpty() ? -1 : Integer.parseInt(capStr), isOnlyOne,
                isOnlyOne ? new Integer[]{-1, -1} : new Integer[]{firstArea.getText().isEmpty() ? -1 : Integer.parseInt(firstArea.getText()),
                        secondArea.getText().isEmpty() ? -1 : Integer.parseInt(secondArea.getText())});
        successfulSaving.setVisible(true);
    }

    @FXML
    void switchToPrimary() {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spaceColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        DataBase.getSpacesList();
        FilteredList<Space> filteredSpacesList = new FilteredList<>(Space.objectsList, p -> true);
        spacesTable.setItems(filteredSpacesList);
    }
}
