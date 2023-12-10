package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.CoterieType;
import com.vladislav.models.DataBase;
import com.vladislav.models.Event;
import com.vladislav.models.TaskType;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCoterieTypeController extends Controller implements Initializable {

    @FXML
    private TextField nameInput;

    @FXML
    private Text warningTitle;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private Text warningDescription;

    @FXML
    private TableView<CoterieType> typeTaskTable;

    @FXML
    private TableColumn<CoterieType, StringProperty> typeTaskColumn;

    @FXML
    private TableColumn<CoterieType, StringProperty> descriptionColumn;

//    @FXML
//    private TableColumn<Event, String> spaceColumn;
//
//    @FXML
//    private TableColumn<Event, StringProperty> startColumn;
//
//    @FXML
//    private TableColumn<Event, String> typeColumn;

    @FXML
    private Text warningType;

    @FXML
    private Text successfulSaving;

    @FXML
    void hideWarnings() {
        warningTitle.setVisible(false);
        warningDescription.setVisible(false);
        warningType.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    void cleanForm() {
        nameInput.setText(null);
        descriptionInput.setText(null);
        typeTaskTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<CoterieType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<CoterieType> taskTypeList = selectionModel.getSelectedItems();

        if (taskTypeList == null || taskTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        if (taskTypeList.size() != 1) {
            warningType.setText("Нужно выбрать только один тип!");
            warningType.setVisible(true);
            return;
        }

        CoterieType type = taskTypeList.get(0);
        nameInput.setText(type.getName());
        descriptionInput.setText(type.getDescription());
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<CoterieType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<CoterieType> coterieTypeList = selectionModel.getSelectedItems();

        if (coterieTypeList == null || coterieTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        for (CoterieType type : coterieTypeList) {
            DataBase.removeCoterieType(type);
        }
    }

    @FXML
    void save() {
        hideWarnings();
        String type = nameInput.getText();
        String description = descriptionInput.getText();

        boolean flag = true;
        if (type == null || type.isEmpty()) {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (description == null || description.isEmpty()) {
            warningDescription.setVisible(true);
            flag = false;
        }
        if (!flag) return;
        DataBase.addCoterieType(type, description);
        successfulSaving.setVisible(true);
    }

    @FXML
    private void close() {
        App.closeSecondWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeTaskColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        DataBase.loadTaskTypeList();
        FilteredList<CoterieType> coterieTypeList = new FilteredList<>(CoterieType.objectsList, p -> true);
        typeTaskTable.setItems(coterieTypeList);
    }
}
