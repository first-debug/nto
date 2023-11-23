package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.DataBase;
import com.vladislav.models.Event;
import com.vladislav.models.Type;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableList;

public class CreateNewEventController extends Controller implements Initializable {

    @FXML
    private TextField typeName;

    @FXML
    private TextField isEntertainmentArea;

    @FXML
    private TextField titleInput;

    @FXML
    private Text warningTitle;

    @FXML
    private TextField descriptionInput;

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
    private TableView<Type> typeTable;

    @FXML
    private TableColumn<Type, StringProperty> type;

    @FXML
    private TableColumn<Type, StringProperty> isEntertainment;
    @FXML
    private Text warningType;

    @FXML
    void addType() {
        String nameType = typeName.getText();
        String typeIsEntertainmentArea = isEntertainmentArea.getText();
        if (nameType == null)
        {
            return;
        }
        if (typeIsEntertainmentArea == null)
        {
            return;
        }
        DataBase.addType(new Type(nameType, typeIsEntertainmentArea));
    }

    @FXML
    void removeType() {
        typeTable.getSelectionModel().getSelectedItems().forEach(DataBase::removeType);
    }

    @FXML
    void cleanForm() {
        titleInput.setText("");
        descriptionInput.setText("");
        timeInput.setValue(LocalDate.now());
        hours.setValue("00");
        minutes.setValue("00");
        typeTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void save(ActionEvent event) {
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        LocalDate date = timeInput.getValue();
        TableView.TableViewSelectionModel<Type> selectionModel = typeTable.getSelectionModel();
        ObservableList<Type> typeList = selectionModel.getSelectedItems();

        boolean flag = true;
        if (title.isEmpty())
        {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (description.isEmpty())
        {
            warningDescription.setVisible(true);
            flag = false;
        }
        if (typeList.isEmpty()) {
            warningType.setVisible(true);
            flag = false;
        }
        try
        {
            if (date.toString().split("-").length != 3) {
                warningDate.setVisible(true);
                flag = false;
            }
        } catch (DateTimeParseException | NullPointerException ex)
        {
            warningDate.setVisible(true);
            System.err.println(ex.getMessage());
            flag = false;
        }
        if (Integer.parseInt(hours.getValue()) > 23 || Integer.parseInt(minutes.getValue()) > 59)
        {
            warningDate.setVisible(true);
            flag = false;
        }
        if (!flag) return;

        Calendar.Builder timeToStart = new Calendar.Builder();
        String[] dateSet = date.toString().split("-");
        int year = Integer.parseInt(dateSet[0]);
        int month = Integer.parseInt(dateSet[1]);
        int day = Integer.parseInt(dateSet[2]);
        int hour = Integer.parseInt(hours.getValue());
        int minute = Integer.parseInt(minutes.getValue());
        timeToStart.setDate(year, month, day);
        timeToStart.setTimeOfDay(hour, minute, 0);

        DataBase.addEvent(title, description, timeToStart.build().getTimeInMillis(), typeList.get(0).getName());

    }

    @FXML
    void hideWarnings()
    {
        warningTitle.setVisible(false);
        warningDescription.setVisible(false);
        warningDate.setVisible(false);
        warningType.setVisible(false);
        // Не работает
    }

    @FXML
    void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
        //App.changeWindowsSize(780, 540); // изменяем размер окна
        // создаём и заполняем таблицу выбора типов
        type.setCellValueFactory(new PropertyValueFactory<>("name"));
        isEntertainment.setCellValueFactory(new PropertyValueFactory<>("isEntertainment"));

        List<Type> typesList = DataBase.getTypes();
        if (!typesList.isEmpty()) {
            ObservableList<Type> list = observableList(typesList);
            typeTable.setItems(list);
        }


        timeInput.setValue(LocalDate.now());

        // заполняем choice box-ы
        ObservableList<String> hoursList = FXCollections.observableList(new ArrayList<>(){{
            for (Integer i = 0; i < 24; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        hours.setItems(hoursList);
        hours.setValue("00");

        ObservableList<String> minutesList = FXCollections.observableList(new ArrayList<>(){{
            for (Integer i = 0; i < 60; i++) {
                if (i < 10){
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        minutes.setItems(minutesList);
        minutes.setValue("00");
    }

}
