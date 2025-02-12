package com.vladislav.controllers.admin;

import com.vladislav.App;
import com.vladislav.controllers.AdminDesktopController;
import com.vladislav.controllers.Controller;
import com.vladislav.models.Booking;
import com.vladislav.models.DataBase;
import com.vladislav.models.Event;
import com.vladislav.models.Space;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminCreateNewBookingController extends Controller implements Initializable {
    private FilteredList<Space> filteredSpacesList;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab eventTab;
    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, StringProperty> timeOfRegColumn;
    @FXML
    private TableColumn<Booking, String> eventNameColumn;
    @FXML
    private TableColumn<Booking, String> bookingSpaceColumn;
    @FXML
    private TableColumn<Booking, StringProperty> bookingStartColumn;
    @FXML
    private TableColumn<Booking, StringProperty> bookingEndColumn;
    @FXML
    private TableColumn<Booking, String> halfOfSpaceColumn;
    @FXML
    private DatePicker dateStartInput;
    @FXML
    private DatePicker dateEndInput;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private ComboBox<String> hoursEnd;
    @FXML
    private ComboBox<String> hoursStart;
    @FXML
    private ComboBox<String> minutesEnd;
    @FXML
    private ComboBox<String> minutesStart;
    @FXML
    private RadioButton isFirst;
    @FXML
    private RadioButton isSecond;
    @FXML
    private Tab spaceTab;
    @FXML
    private TableView<Space> spacesTable;
    @FXML
    private TableColumn<Space, String> secondAreaColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> capacityColumn;
    @FXML
    private TableColumn<Space, IntegerProperty> areaColumn;
    @FXML
    private TableColumn<Space, Integer> firstAreaColumn;
    @FXML
    private TableColumn<Space, StringProperty> spaceDescriptionColumn;
    @FXML
    private TableColumn<Space, StringProperty> spaceNameColumn;
    @FXML
    private Text successfulSaving;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, String> typeColumn;

    @FXML
    private TableColumn<Event, StringProperty> eventColumn;

    @FXML
    private TableColumn<Event, StringProperty> eventDescriptionColumn;

    @FXML
    private TableColumn<Event, String> spaceColumn;

    @FXML
    private TableColumn<Event, StringProperty> startColumn;

    @FXML
    private Text warningBooking;

    @FXML
    private Text warningDateEnd;

    @FXML
    private Text warningDateStart;

    @FXML
    private Text warningDescription;

    @FXML
    private Text warningEvent;

    @FXML
    private Text warningSpace;
    
    @FXML
    private void checkDate() {
        LocalDate dateStart = dateStartInput.getValue();
        LocalDate dateEnd = dateEndInput.getValue();
        if (dateStart != null && dateEnd != null) {
            Calendar timeToStart = null;
            Calendar timeToEnd = null;
            boolean flag = true;
            try {
                if (dateStart.toString().split("-").length != 3 |
                        dateEnd.toString().split("-").length != 3) throw new NullPointerException();
                timeToStart = parseDate(dateStart, hoursStart.getValue(), minutesStart.getValue());
                timeToEnd = parseDate(dateEnd, hoursEnd.getValue(), minutesEnd.getValue());
            } catch (NullPointerException ex) {
                warningDateStart.setText("Дата или время заняты!");
                warningDateStart.setVisible(true);
                flag = false;
            }
            if (!flag) return;
            if (timeToStart.before(timeToEnd)) {
                warningDateStart.setVisible(true);
                warningDateEnd.setVisible(true);
                warningDateStart.setText("Нужно ввести корректную дату и время!");
                warningDateEnd.setText("Нужно ввести корректную дату и время!");
                return;
            }

            Calendar finalTimeToStart = timeToStart;
            Calendar finalTimeToEnd = timeToEnd;
            filteredSpacesList.setPredicate(space -> {
                for (Booking booking : space.getBookingList()) {
                    Calendar tStart = (Calendar.getInstance());
                    tStart.setTime(new Date(booking.getTimeOfStart()));

                    Calendar tEnd = (Calendar.getInstance());
                    tEnd.setTime(new Date(booking.getTimeOfEnd()));
                    if (!finalTimeToStart.after(tStart) || !finalTimeToEnd.after(tEnd)) return false;
                }
                return true;
            });
        } else if (dateStart != null) {
            Calendar timeToStart = null;
            boolean flag = true;
            try {
                if (dateStart.toString().split("-").length != 3) throw new NullPointerException();
                timeToStart = parseDate(dateStart, hoursStart.getValue(), minutesStart.getValue());
            } catch (NullPointerException ex) {
                warningDateStart.setVisible(true);
                flag = false;
            }
            if (!flag) return;

            Calendar finalTimeToStart = timeToStart;
            filteredSpacesList.setPredicate(space -> {
                for (Booking booking : space.getBookingList()) {
                    Calendar tStart = (Calendar.getInstance());
                    tStart.setTime(new Date(booking.getTimeOfStart()));
                    if (!finalTimeToStart.after(tStart)) return false;
                }
                return true;
            });
        } else if (dateEnd != null) {
            Calendar timeToEnd = null;
            boolean flag = true;
            try {
                if (dateEnd.toString().split("-").length != 3) throw new NullPointerException();
                timeToEnd = parseDate(dateEnd, hoursEnd.getValue(), minutesEnd.getValue());
            } catch (NullPointerException ex) {
                warningDateEnd.setText("Дата или время заняты!");
                warningDateEnd.setVisible(true);
                flag = false;
            }
            if (!flag) return;

            Calendar finalTimeToEnd = timeToEnd;
            filteredSpacesList.setPredicate(space -> {
                for (Booking booking : space.getBookingList()) {
                    Calendar tEnd = (Calendar.getInstance());
                    tEnd.setTime(new Date(booking.getTimeOfEnd()));
                    if (finalTimeToEnd.after(tEnd)) return false;
                }
                return true;
            });
        }
    }

    @FXML
    void cleanForm() {
        descriptionInput.setText(null);
        dateStartInput.setValue(null);
        dateEndInput.setValue(null);
        hoursStart.setValue(null);
        minutesStart.setValue(null);
        hoursEnd.setValue(null);
        minutesEnd.setValue(null);
        eventsTable.getSelectionModel().clearSelection();
        spacesTable.getSelectionModel().clearSelection();
        bookingTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void fixMinutes() {
        if (minutesStart.getValue() == null || minutesStart.getValue().isEmpty()) {
            minutesStart.setValue(null);
            return;
        }
        if (minutesStart.getValue() != null && Integer.parseInt(minutesStart.getValue()) > 59) {
            minutesStart.setValue("59");
        }
        if (minutesEnd.getValue() == null || minutesEnd.getValue().isEmpty()) {
            minutesEnd.setValue(null);
            return;
        }
        if (minutesEnd.getValue() != null && Integer.parseInt(minutesEnd.getValue()) > 59) {
            minutesEnd.setValue("59");
        }
    }

    @FXML
    void fixHours() {
        if (hoursStart.getValue() == null || hoursStart.getValue().isEmpty()) {
            hoursStart.setValue(null);
            return;
        }
        if (hoursEnd.getValue() != null && Integer.parseInt(hoursEnd.getValue()) > 23) {
            hoursEnd.setValue("23");
        }
    }

    @FXML
    void fixFirstHalf() {
        if (!isSecond.isSelected() && !isFirst.isSelected()) isFirst.setSelected(true);
    }

    @FXML
    void fixSecondHalf() {
        if (!isSecond.isSelected() && !isFirst.isSelected()) isSecond.setSelected(true);
    }

    @FXML
    void hideWarnings() {
        warningBooking.setVisible(false);
        warningDateStart.setVisible(false);
        warningDateEnd.setVisible(false);
        warningDateStart.setText(null);
        warningDateEnd.setText(null);
        warningDescription.setVisible(false);
        warningEvent.setVisible(false);
        warningSpace.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    void save() {
        hideWarnings();
        String comment = descriptionInput.getText();
        LocalDate dateStart = dateStartInput.getValue();
        LocalDate dateEnd = dateEndInput.getValue();
        boolean isIsFirst = isFirst.isSelected();
        boolean isIsSecond = isSecond.isSelected();
        int spaceParts;
        Calendar timeToStart = null;
        Calendar timeToEnd = null;
        TableView.TableViewSelectionModel<Event> selectedEvents = eventsTable.getSelectionModel();
        ObservableList<Event> eventsList = selectedEvents.getSelectedItems();
        TableView.TableViewSelectionModel<Space> selectedSpaces = spacesTable.getSelectionModel();
        ObservableList<Space> spaceList = selectedSpaces.getSelectedItems();

        boolean flag = true;
        if (comment == null || comment.isEmpty()) {
            warningDescription.setVisible(true);
            flag = false;
        }
        if (eventsList == null || eventsList.isEmpty()) {
            tabPane.getSelectionModel().select(eventTab);
            warningEvent.setVisible(true);
            flag = false;
        }
        if (spaceList == null || spaceList.isEmpty()) {
            tabPane.getSelectionModel().select(spaceTab);
            warningSpace.setVisible(true);
            flag = false;
        }
        if (isIsFirst && isIsSecond) spaceParts = 3;
        else spaceParts = isIsFirst ? 1 : 2;
        try {
            if (dateStart.toString().split("-").length != 3 |
                    dateEnd.toString().split("-").length != 3) throw new NullPointerException();
            timeToStart = parseDate(dateStart, hoursStart.getValue(), minutesStart.getValue());
            timeToEnd = parseDate(dateEnd, hoursEnd.getValue(), minutesEnd.getValue());
        } catch (NullPointerException ex) {
            warningDateStart.setText("Нужно ввести корректную дату и время!");
            warningDateStart.setVisible(true);
            flag = false;
        }
        if (!flag) return;

        DataBase.addBooking(System.currentTimeMillis(), eventsList.get(0), timeToStart.getTimeInMillis(),
                timeToEnd.getTimeInMillis(), spaceList.get(0), spaceParts, comment);
        successfulSaving.setVisible(true);
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Booking> selectionModel = bookingTable.getSelectionModel();
        ObservableList<Booking> bookingList = selectionModel.getSelectedItems();

        if (bookingList == null || bookingList.isEmpty()) {
            warningBooking.setVisible(true);
            return;
        }
        bookingList.forEach(DataBase::removeBooking);
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<Booking> selectionModel = bookingTable.getSelectionModel();
        ObservableList<Booking> selectedBookings = selectionModel.getSelectedItems();
        if (selectedBookings == null || selectedBookings.isEmpty()) {
            warningBooking.setVisible(true);
            return;
        }
        if (selectedBookings.size() == 1) {
            Booking booking = selectedBookings.get(0);
            descriptionInput.setText(booking.getComment());

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(new Date(booking.getTimeOfStart()));
            dateStartInput.setValue(calendarStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            hoursStart.setValue(String.valueOf(calendarStart.get(Calendar.HOUR_OF_DAY)));
            minutesStart.setValue(String.valueOf(calendarStart.get(Calendar.MINUTE)));

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(new Date(booking.getTimeOfEnd()));
            dateEndInput.setValue(calendarEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            hoursEnd.setValue(String.valueOf(calendarEnd.get(Calendar.HOUR_OF_DAY)));
            minutesEnd.setValue(String.valueOf(calendarEnd.get(Calendar.MINUTE)));

            spacesTable.getSelectionModel().select(booking.getSpace());
            spacesTable.getSelectionModel().select(booking.getSpace());
        }

    }

    @FXML
    void switchToEditEvents() {
//        App.setRoot("createNewEvent", new AdminCreateNewEventController());
    }

    @FXML
    void switchToEditSpaces() {
//        App.setRoot("createNewSpace", new AdminCreateNewSpaceController());
    }

    @FXML
    void switchToPrimary() {
//        App.setRoot("adminDesktop", new AdminDesktopController());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ComboBox - часы
        ObservableList<String> hoursList = FXCollections.observableList(new ArrayList<String>() {{
            for (Integer i = 0; i < 24; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        hoursStart.setItems(hoursList);
        hoursStart.setValue(null);
        hoursEnd.setItems(hoursList);
        hoursEnd.setValue(null);

        // ComboBox - минуты
        ObservableList<String> minutesList = FXCollections.observableList(new ArrayList<String>() {{
            for (Integer i = 0; i < 60; i++) {
                if (i < 10) {
                    add('0' + i.toString());
                } else add(i.toString());
            }
        }});
        minutesStart.setItems(minutesList);
        minutesStart.setValue(null);
        minutesEnd.setItems(minutesList);
        minutesEnd.setValue(null);

        spaceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spaceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        firstAreaColumn.setCellValueFactory(new PropertyValueFactory<>("firstArea"));
        secondAreaColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getSecondArea() == -1 ?
                        "Только одна часть" : String.valueOf(cell.getValue().getSecondArea()))
        );
        DataBase.loadSpacesList("event");
        filteredSpacesList = new FilteredList<>(Space.objectsList, p -> p.getType().equals("event"));
        spacesTable.setItems(filteredSpacesList);

        eventColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        startColumn.setCellValueFactory(new PropertyValueFactory<>("stringTime"));
        typeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        DataBase.loadEvents(null);
        FilteredList<Event> filteredEventList = new FilteredList<>(Event.objectsList, p -> true);
        eventsTable.setItems(filteredEventList);

        timeOfRegColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfRegString"));
        eventNameColumn.setCellValueFactory(cell -> cell.getValue().getEvent().titleProperty());
        bookingSpaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        bookingStartColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfStartString"));
        bookingEndColumn.setCellValueFactory(new PropertyValueFactory<>("timeOfEndString"));
        halfOfSpaceColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getHalfOfSpace() == 3 ? "Полностью" : String.valueOf(cell.getValue().getHalfOfSpace()))
        );
        DataBase.loadBookingList(null);
        FilteredList<Booking> filteredBookingList = new FilteredList<>(Booking.objectsList, p -> true);
        bookingTable.setItems(filteredBookingList);
    }
}

