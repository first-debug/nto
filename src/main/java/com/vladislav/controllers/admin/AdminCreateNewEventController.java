package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.controllers.EditEventTypeController;
import com.vladislav.models.DataBase;
import com.vladislav.models.EventType;
import com.vladislav.models.Space;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableList;

public class AdminCreateNewEventController extends Controller implements Initializable {

    @FXML
    private TextField titleInput;
    @FXML
    private Text warningTitle;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private Text warningDescription;

    @FXML
    private DatePicker timeInput;

    @FXML
    private Text warningDate;

    @FXML
    private ComboBox<String> hours;

    @FXML
    private ComboBox<String> minutes;

    @FXML
    private Text warningSpace;

    @FXML
    private TableView<EventType> typeTable;

    @FXML
    private TableColumn<EventType, StringProperty> type;

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
    private Text warningType;

    @FXML
    private Text successfulSaving;

    @FXML
    private void switchToPrimary() {
        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @FXML
    private void switchToEditEventTypes() {
        hideWarnings();
        App.newWindow("editEventType", new EditEventTypeController(), "Редактирование видов мероприятий",
                505, 490);
    }

    @FXML
    private void switchToEditSpaces() {
        hideWarnings();
//        App.newWindow("editSpaces", new EditSpacesController(), "Редактирование помещений");
    }

    @FXML
    void fixMinutes() {
        if (minutes.getValue() == null || minutes.getValue().isEmpty()) {
            minutes.setValue("00");
            return;
        }
        if (minutes.getValue() != null && Integer.parseInt(minutes.getValue()) > 59) {
            minutes.setValue("59");
        }
    }

    @FXML
    void fixHours() {
        if (hours.getValue() == null || hours.getValue().isEmpty()) {
            hours.setValue("00");
            return;
        }
        if (hours.getValue() != null && Integer.parseInt(hours.getValue()) > 23) {
            hours.setValue("23");
        }
    }

    @FXML
    void removeType() {
        hideWarnings();
        typeTable.getSelectionModel().getSelectedItems().forEach(DataBase::removeEventType);
    }

    @FXML
    void cleanForm() {
        titleInput.setText(null);
        descriptionInput.setText(null);
        timeInput.setValue(null);
        hours.setValue(null);
        minutes.setValue(null);
        typeTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void hideWarnings() {
        warningTitle.setVisible(false);
        warningDescription.setVisible(false);
        warningDate.setVisible(false);
        warningType.setVisible(false);
        warningSpace.setVisible(false);
        successfulSaving.setVisible(false);
    }
    @FXML
    void save() {
        hideWarnings();
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        LocalDate date = timeInput.getValue();
        Calendar timeToStart = null;
        TableView.TableViewSelectionModel<EventType> selectedTypes = typeTable.getSelectionModel();
        ObservableList<EventType> eventTypeList = selectedTypes.getSelectedItems();
        TableView.TableViewSelectionModel<Space> selectedSpaces = spacesTable.getSelectionModel();
        ObservableList<Space> spaceList = selectedSpaces.getSelectedItems();

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
        if (eventTypeList == null || eventTypeList.isEmpty()) {
            warningType.setVisible(true);
            flag = false;
        }
        if (spaceList == null || spaceList.isEmpty()) {
            warningSpace.setVisible(true);
            flag = false;
        }
        try
        {
            if (date.toString().split("-").length != 3) {
                warningDate.setVisible(true);
                flag = false;
            }

            Calendar.Builder dateBuilder = new Calendar.Builder();
            String[] dateSet = date.toString().split("-");
            int year = Integer.parseInt(dateSet[0]);
            int month = Integer.parseInt(dateSet[1]);
            int day = Integer.parseInt(dateSet[2]);
            int hour;
            if (hours.getValue().isEmpty()) hour = 0;
            else hour = Integer.parseInt(minutes.getValue());
            int minute;
            if (minutes.getValue().isEmpty()) minute = 0;
            else minute = Integer.parseInt(minutes.getValue());
            dateBuilder.setDate(year, month, day);
            dateBuilder.setTimeOfDay(hour, minute, 0);
            timeToStart = dateBuilder.build();
        } catch (NullPointerException ex)
        {
            warningDate.setVisible(true);
            flag = false;
        }
        if (flag && !timeToStart.after(System.currentTimeMillis())) {
            warningDate.setVisible(true);
            return;
        }
        if (!flag) return;

        DataBase.addEvent(title, description, spaceList.get(0), timeToStart.getTimeInMillis(), eventTypeList.get(0));
        successfulSaving.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // задаём настройки таблицы выбора типов и заполняем её
        type.setCellValueFactory(new PropertyValueFactory<>("name"));

        List<EventType> typesList = DataBase.getTypesEventList();
        if (!typesList.isEmpty()) {
            ObservableList<EventType> list = observableList(typesList);
            typeTable.setItems(list);
        }

        spaceColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        List<Space> spacesList = DataBase.getSpacesList();
        if (!spacesList.isEmpty()) {
            ObservableList<Space> list = observableList(spacesList);
            spacesTable.setItems(list);
        }

        // ComboBox - часы
        ObservableList<String> hoursList = FXCollections.observableList(new ArrayList<>(){{
            for (Integer i = 0; i < 24; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        hours.setItems(hoursList);
        hours.setValue(null);

        // ComboBox - минуты
        ObservableList<String> minutesList = FXCollections.observableList(new ArrayList<>(){{
            for (Integer i = 0; i < 60; i++) {
                if (i < 10){
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        minutes.setItems(minutesList);
        minutes.setValue(null);
    }
}

