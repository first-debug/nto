package com.vladislav.controllers.primary;

import com.vladislav.controllers.Controller;
import com.vladislav.models.DataBase;
import com.vladislav.models.EventType;
import com.vladislav.models.Space;
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

public class CreateNewEventController extends Controller implements Initializable {

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
    private ChoiceBox<String> selectSpace;

    @FXML
    private Text warningSpace;

    @FXML
    private TableView<EventType> typeTable;

    @FXML
    private TableColumn<EventType, StringProperty> type;

    @FXML
    private TableColumn<EventType, StringProperty> isEntertainment;

    @FXML
    private TextField typeName;

    @FXML
    private ComboBox<String> isEntertainmentArea;

    @FXML
    private Text warningType;

    @FXML
    private Text warningAddType;

    @FXML
    void fixMinutes() {
        if (minutes.getValue().isEmpty()) {
            minutes.setValue("00");
            return;
        }
        if (minutes.getValue() != null && Integer.parseInt(minutes.getValue()) > 59) {
            minutes.setValue("59");
        }
    }

    @FXML
    void fixHours() {
        if (hours.getValue().isEmpty()) {
            hours.setValue("00");
            return;
        }
        if (hours.getValue() != null && Integer.parseInt(hours.getValue()) > 23) {
            hours.setValue("23");
        }
    }

    @FXML
    void addType() {
        String nameType = typeName.getText();
        String typeIsEntertainmentArea = isEntertainmentArea.getValue();

        if (nameType.isEmpty())
        {
            warningAddType.setText("Нужно ввести название!");
            warningAddType.setVisible(true);
            return;
        }
        if (typeIsEntertainmentArea == null)
        {
            warningAddType.setText("Нужно указать является ли тип развлекательным!");
            warningAddType.setVisible(true);
            return;
        }
        // Нужна поверка и текст предупереждения
        DataBase.addEventType(nameType, typeIsEntertainmentArea.equalsIgnoreCase("да"));
        typeName.setText(null);
        isEntertainmentArea.setValue(null);
    }

    @FXML
    void removeType() {
        typeTable.getSelectionModel().getSelectedItems().forEach(DataBase::removeEventType);
    }

    @FXML
    void cleanForm() {
        titleInput.setText(null);
        descriptionInput.setText(null);
        timeInput.setValue(null);
        hours.setValue(null);
        minutes.setValue(null);
        selectSpace.setValue(null);
        typeTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void hideWarnings()
    {
        warningTitle.setVisible(false);
        warningDescription.setVisible(false);
        warningDate.setVisible(false);
        warningType.setVisible(false);
        warningSpace.setVisible(false);
        warningAddType.setVisible(false);
        warningAddType.setText("");
    }
    @FXML
    void save() {
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        LocalDate date = timeInput.getValue();
        Calendar timeToStart = null;
        TableView.TableViewSelectionModel<EventType> selectionModel = typeTable.getSelectionModel();
        ObservableList<EventType> eventTypeList = selectionModel.getSelectedItems();
        String spaceString = selectSpace.getValue();

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
        if (spaceString == null || spaceString.isEmpty()) {
            warningSpace.setVisible(true);
            flag = false;
        }
        if (!flag) return; // выше последней проверки для того, чтобы знать наверняка, что timeToStart != null
        if (!timeToStart.after(System.currentTimeMillis())) {
            warningDate.setVisible(true);
            return;
        }
        Space space = DataBase.getSpace(spaceString);

        DataBase.addEvent(title, description, space.getId(), timeToStart.getTimeInMillis(), eventTypeList.get(0));
    }

    @Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
        // задаём настройки таблицы выбора типов и заполняем её
        type.setCellValueFactory(new PropertyValueFactory<>("name"));
        isEntertainment.setCellValueFactory(new PropertyValueFactory<>("isEntertainmentString"));

        List<EventType> typesList = DataBase.getTypesEventList();
        if (!typesList.isEmpty()) {
            ObservableList<EventType> list = observableList(typesList);
            typeTable.setItems(list);
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

        // ComboBox - развлекательный ли новый тип
        isEntertainmentArea.setItems(FXCollections.observableList(new ArrayList<>(){{
            add("Да");
            add("Нет");
        }}));

        ArrayList<Space> spacesList = DataBase.getSpacesList();
        ArrayList<String> spaceStringsList = new ArrayList<>(){{
            for (Space sp : spacesList) add(sp.getName());
        }};
        if (!spacesList.isEmpty()) {
            selectSpace.setItems(FXCollections.observableList(spaceStringsList));
        }
    }
}
