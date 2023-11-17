package com.vladislav;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import com.vladislav.models.Event;
import com.vladislav.models.Type;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EntertainmentsController implements Initializable {

    @FXML
    private TableView<Event> tableOfEvents;

    @FXML
    private TableColumn<Event, StringProperty> nameColumn;

    @FXML
    private TableColumn<Event, StringProperty> descriptionColumn;

    @FXML
    private TableColumn<Event, LongProperty> startClolumn;

    @FXML
    private TableColumn<Event, LongProperty> endColumn;

    @FXML
    private TableColumn<Event, Property<Type>> typeColumn;
    
    @FXML
    private Button goBackButton;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Event, StringProperty>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Event, StringProperty>("description"));
        startClolumn.setCellValueFactory(new PropertyValueFactory<Event, LongProperty>("timeToStart"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Event, Property<Type>>("type"));

        Calendar time = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.YYYY");
        ObservableList<Event> listOfStrings = FXCollections.observableArrayList();
        
        time.set(2023, 11, 30, 19, 30);
        listOfStrings.add(new Event(
                "Лекция: Коренные народы севера",
                "Вы узнаете много интересное, погрузитесь в удивительный мир, где солнце светит торлько летом",
                format.format(new Date(time.getTimeInMillis())),
                Type.LECTURE));

        time.set(2023, 11, 20, 14, 00);
        listOfStrings.add(new Event(
                "Терракотовая армия. Бессмертные воины Китая",
                "Выставка является значительным и масштабным культурным событием года. Экспозиция основана на археологической сенсации XX века - глиняной армии китайского императора Цинь Шихуанди, правившего в III веке до нашей эры. Китайская Терракотовая армия является объектом Всемирного наследия ЮНЕСКО и по праву может считаться восьмым чудом света.",
                format.format(new Date(time.getTimeInMillis())),
                Type.EXHIBITION));
                
        time.set(2023, 11, 28, 17, 00);        
        listOfStrings.add(new Event(
                "50 Years Of Rock",
                "5 десятилетий рок-музыки XX века, юбилей «Белого Альбома» The Beatles!",
                format.format(new Date(time.getTimeInMillis())),
                Type.CONCERT));
        tableOfEvents.setItems(listOfStrings);
    }
}