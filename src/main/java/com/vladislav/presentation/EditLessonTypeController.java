package com.vladislav.presentation;

import com.vladislav.domain.LessonType;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.presentation.services.WindowService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class EditLessonTypeController extends Controller implements Initializable {

    @FXML
    private TextField nameInput;

    @FXML
    private Text warningTitle;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private Text warningDescription;

    @FXML
    private TableView<LessonType> typeTaskTable;

    @FXML
    private TableColumn<LessonType, StringProperty> typeTaskColumn;

    @FXML
    private TableColumn<LessonType, StringProperty> descriptionColumn;

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

    public EditLessonTypeController(@Autowired WindowService windowService) {
        super(windowService);
    }

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
        TableView.TableViewSelectionModel<LessonType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<LessonType> taskTypeList = selectionModel.getSelectedItems();

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

        LessonType type = taskTypeList.get(0);
        nameInput.setText(type.getName());
        descriptionInput.setText(type.getDescription());
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<LessonType> selectionModel = typeTaskTable.getSelectionModel();
        ObservableList<LessonType> lessonTypeList = selectionModel.getSelectedItems();

        if (lessonTypeList == null || lessonTypeList.isEmpty()) {
            warningType.setText("Нужно выбрать тип!");
            warningType.setVisible(true);
            return;
        }
        for (LessonType type : lessonTypeList) {
            DataBase.removeLessonType(type);
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
        DataBase.addLessonType(type, description);
        successfulSaving.setVisible(true);
    }

    @FXML
    private void close() {
        windowService.closeSecondWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeTaskColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        DataBase.loadTaskTypeList();
        FilteredList<LessonType> lessonTypeList = new FilteredList<>(LessonType.objectsList, p -> true);
        typeTaskTable.setItems(lessonTypeList);
    }
}
