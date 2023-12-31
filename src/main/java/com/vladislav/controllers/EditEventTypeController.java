package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.DataBase;
import com.vladislav.models.EventType;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EditEventTypeController extends Controller implements Initializable {

    @FXML
    private TextField nameInput;

    @FXML
    private Text warningTitle;

    @FXML
    private RadioButton yesCheck;

    @FXML
    private RadioButton noCheck;

    @FXML
    private Text warningEntertainment;

    @FXML
    private TableView<EventType> typeEventTable;

    @FXML
    private TableColumn<EventType, StringProperty> typeEventColumn;

    @FXML
    private TableColumn<EventType, StringProperty> isEntertainmentColumn;

    @FXML
    private Text warningType;

    @FXML
    private Text successfulSaving;

    @FXML
    void hideWarnings() {
        warningTitle.setVisible(false);
        warningEntertainment.setVisible(false);
        warningType.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    void checkYesStatus() {
        hideWarnings();
        noCheck.setSelected(false);
        if (!yesCheck.isSelected()) yesCheck.setSelected(true);
    }

    @FXML
    void checkNoStatus() {
        hideWarnings();
        yesCheck.setSelected(false);
        if (!noCheck.isSelected()) noCheck.setSelected(true);
    }

    @FXML
    void cleanForm() {
        nameInput.setText(null);
        yesCheck.setSelected(true);
        noCheck.setSelected(false);
        typeEventTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<EventType> selectionModel = typeEventTable.getSelectionModel();
        ObservableList<EventType> eventTypeList = selectionModel.getSelectedItems();

        if (eventTypeList == null || eventTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        if (eventTypeList.size() != 1) {
            warningType.setText("Нужно выбрать только один тип!");
            warningType.setVisible(true);
            return;
        }

        EventType type = eventTypeList.get(0);
        nameInput.setText(type.getName());
        boolean isEntertainment = type.getIsEntertainment();
        yesCheck.setSelected(isEntertainment);
        noCheck.setSelected(!isEntertainment);
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<EventType> selectionModel = typeEventTable.getSelectionModel();
        ObservableList<EventType> eventTypeList = selectionModel.getSelectedItems();

        if (eventTypeList == null || eventTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        eventTypeList.forEach(DataBase::removeEventType);
    }

    @FXML
    void save() {
        hideWarnings();
        String type = nameInput.getText();
        boolean isEntertainment = yesCheck.isSelected();
        boolean noIsEntertainment = noCheck.isSelected();

        boolean flag = true;
        if (type == null || type.isEmpty()) {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (!isEntertainment && noIsEntertainment || isEntertainment && !noIsEntertainment) {
            warningEntertainment.setVisible(true);
            flag = false;
        }
        if (!flag) return;
        DataBase.addEventType(type, isEntertainment);
        successfulSaving.setVisible(true);
    }

    @FXML
    private void close() {
        App.closeSecondWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yesCheck.setSelected(true);

        typeEventColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        isEntertainmentColumn.setCellValueFactory(new PropertyValueFactory<>("isEntertainmentString"));
        DataBase.loadEventTypesList();
        FilteredList<EventType> typesList = new FilteredList<>(EventType.objectsList, p -> true);
        typeEventTable.setItems(typesList);
    }
}
