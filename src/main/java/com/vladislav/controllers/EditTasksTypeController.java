package com.vladislav.controllers;

import com.vladislav.models.DataBase;
import com.vladislav.models.TaskType;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class EditTasksTypeController extends Controller {

    @FXML
    private TextField nameInput;

    @FXML
    private Text warningTitle;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private Text warningDescription;

    @FXML
    private TableView<TaskType> typeTaskTable;

    @FXML
    private TableColumn<TaskType, StringProperty> typeColumn;

    @FXML
    private TableColumn<TaskType, StringProperty> descriptionColumn;

    @FXML
    private Text warningType;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

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
        TableView.TableViewSelectionModel<TaskType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<TaskType> taskTypeList = selectionModel.getSelectedItems();

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

        TaskType type = taskTypeList.get(0);
        nameInput.setText(type.getName());
        descriptionInput.setText(type.getDescription());
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<TaskType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<TaskType> taskTypeList = selectionModel.getSelectedItems();

        if (taskTypeList == null || taskTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        for (TaskType type : taskTypeList) {
            String name = type.getName();
            DataBase.removeTaskType(type);
            TaskType.objectsList.removeIf(f -> (f.getName().equalsIgnoreCase(name)));
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
        DataBase.addTaskType(type, description);
        successfulSaving.setVisible(true);
    }
}
