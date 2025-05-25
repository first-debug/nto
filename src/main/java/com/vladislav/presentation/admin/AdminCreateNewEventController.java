package com.vladislav.presentation.admin;

import com.vladislav.presentation.services.WindowService;
import com.vladislav.presentation.Controller;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.domain.Event;
import com.vladislav.domain.EventType;
import com.vladislav.domain.Space;
import com.vladislav.presentation.EditEventTypeController;
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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    private DatePicker dateInput;

    @FXML
    private Text warningDate;

    @FXML
    private ComboBox<String> hours;

    @FXML
    private ComboBox<String> minutes;

    @FXML
    private TableView<EventType> typeTable;

    @FXML
    private TableColumn<EventType, StringProperty> type;

    @FXML
    private Text warningType;

    @FXML
    private TableView<Space> spacesTable;

    @FXML
    private TableColumn<Space, StringProperty> spaceNameColumn;

    @FXML
    private TableColumn<Space, StringProperty> spaceDescriptionColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;

    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;

    @FXML
    private Text warningSpace;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, StringProperty> eventColumn;

    @FXML
    private TableColumn<Event, StringProperty> eventDescriptionColumn;

    @FXML
    private TableColumn<Event, String> spaceColumn;

    @FXML
    private TableColumn<Event, StringProperty> startColumn;

    @FXML
    private TableColumn<Event, String> typeColumn;

    @FXML
    private Text warningEvent;

    @FXML
    private Text successfulSaving;

    public AdminCreateNewEventController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    private void switchToPrimary() {
        windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
    }

    @FXML
    private void switchToEditEventTypes() {
        hideWarnings();
        windowService.createNewWindow("editEventType", new EditEventTypeController(windowService),
                "Редактирование видов мероприятий",505, 490);
    }

    @FXML
    private void switchToEditSpaces() {
        windowService.changeRootStage("createNewSpace", new AdminCreateNewSpaceController(windowService));
    }

    @FXML
    void fixMinutes() {
        if (minutes.getValue() == null || minutes.getValue().isEmpty()) {
            minutes.setValue(null);
            return;
        }
        if (minutes.getValue() != null && Integer.parseInt(minutes.getValue()) > 59) {
            minutes.setValue("59");
        }
    }

    @FXML
    void fixHours() {
        if (hours.getValue() == null || hours.getValue().isEmpty()) {
            hours.setValue(null);
            return;
        }
        if (hours.getValue() != null && Integer.parseInt(hours.getValue()) > 23) {
            hours.setValue("23");
        }
    }

    @FXML
    void edit() {
        TableView.TableViewSelectionModel<Event> selectedEvents = eventsTable.getSelectionModel();
        ObservableList<Event> eventList = selectedEvents.getSelectedItems();
        if (eventList.size() != 0) {
            Event event = eventList.get(0);
            titleInput.setText(event.getTitle());
            descriptionInput.setText(event.getDescription());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(event.getTimeToStart()));
            dateInput.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            hours.setValue(String.valueOf(calendar.get(Calendar.HOUR)));
            minutes.setValue(String.valueOf(calendar.get(Calendar.MINUTE)));
            typeTable.getSelectionModel().select(event.getType());
            spacesTable.getSelectionModel().select(event.getSpace());
        } else {
            warningEvent.setVisible(true);
        }
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Event> selectionModel = eventsTable.getSelectionModel();
        ObservableList<Event> eventsList = selectionModel.getSelectedItems();

        if (eventsList == null || eventsList.isEmpty()) {
            warningEvent.setVisible(true);
            return;
        }
        eventsList.forEach(DataBase::removeEvent);
    }

    @FXML
    void cleanForm() {
        titleInput.setText(null);
        descriptionInput.setText(null);
        dateInput.setValue(null);
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
        LocalDate date = dateInput.getValue();
        Calendar timeToStart = null;
        TableView.TableViewSelectionModel<EventType> selectedTypes = typeTable.getSelectionModel();
        ObservableList<EventType> eventTypeList = selectedTypes.getSelectedItems();
        TableView.TableViewSelectionModel<Space> selectedSpaces = spacesTable.getSelectionModel();
        ObservableList<Space> spaceList = selectedSpaces.getSelectedItems();

        boolean flag = true;
        if (title == null || title.isEmpty()) {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (description == null || description.isEmpty()) {
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
        try {
            if (date.toString().split("-").length != 3) throw new NullPointerException();

            Calendar.Builder dateBuilder = new Calendar.Builder();
            String[] dateSet = date.toString().split("-");
            int year = Integer.parseInt(dateSet[0]);
            int month = Integer.parseInt(dateSet[1]);
            int day = Integer.parseInt(dateSet[2]);
            int hour;
            if (hours.getValue() == null) hour = 0;
            else hour = Integer.parseInt(hours.getValue());
            int minute;
            if (minutes.getValue() == null) minute = 0;
            else minute = Integer.parseInt(minutes.getValue());
            dateBuilder.setDate(year, month - 1, day);
            dateBuilder.setTimeOfDay(hour, minute, 0);
            timeToStart = dateBuilder.build();
        } catch (NullPointerException ex) {
            warningDate.setVisible(true);
            flag = false;
        }
        if (flag && timeToStart.before(Calendar.getInstance(new Locale("rus")))) {
            warningDate.setVisible(true);
            return;
        }
        if (!flag) return;
        DataBase.addEvent(title, description, spaceList.get(0), timeToStart.getTimeInMillis(), eventTypeList.get(0),
                spaceList.get(0).getSeats());
        successfulSaving.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // задаём настройки таблицы выбора типов и заполняем её
        type.setCellValueFactory(new PropertyValueFactory<>("name"));
        FilteredList<EventType> filteredEventTypeList = new FilteredList<>(EventType.objectsList, p -> true);
        typeTable.setItems(filteredEventTypeList);

        // задаём настройки таблицы выбора места проведения и заполняем её
        spaceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spaceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        DataBase.loadSpacesList("event");
        FilteredList<Space> filteredSpaceList = new FilteredList<>(Space.objectsList, p -> p.getType().equals("event"));
        spacesTable.setItems(filteredSpaceList);

        // задаём настройки таблицы выбора мероприятий и заполняем её
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        DataBase.loadEvents(null);
        FilteredList<Event> filteredEventList = new FilteredList<>(Event.objectsList, p -> true);
        eventsTable.setItems(filteredEventList);
        // ComboBox - часы
        ObservableList<String> hoursList = FXCollections.observableList(new ArrayList<>() {{
            for (Integer i = 0; i < 24; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        hours.setItems(hoursList);
        hours.setValue(null);

        // ComboBox - минуты
        ObservableList<String> minutesList = FXCollections.observableList(new ArrayList<>() {{
            for (Integer i = 0; i < 60; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        minutes.setItems(minutesList);
        minutes.setValue(null);
    }
}

