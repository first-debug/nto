package com.vladislav.presentation.admin;

import com.vladislav.application.ApplicationService;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.presentation.AdminDesktopController;
import com.vladislav.presentation.Controller;
import com.vladislav.domain.*;
import com.vladislav.presentation.EditLessonTypeController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class AdminEditLessonsController extends Controller implements Initializable {

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private String separator = null;

    @FXML
    private TextField titleInput;

    @FXML
    private ComboBox<Teacher> teacherBox;

    @FXML
    private DatePicker dateInput;

    @FXML
    private TableView<LessonType> typeTable;

    @FXML
    private TableColumn<LessonType, StringProperty> type;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab spaceTab;

    @FXML
    private Tab lessonTab;

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
    private TableView<Lesson> lessonsTable;

    @FXML
    private TableColumn<Lesson, StringProperty> lessonColumn;

//    @FXML
//    private TableColumn<Lesson, StringProperty> lessonsDescriptionColumn;

    @FXML
    private TableColumn<Lesson, String> spaceColumn;

    @FXML
    private TableColumn<Lesson, String> startColumn;

    @FXML
    private TableColumn<Lesson, String> typeColumn;

    @FXML
    private TextField monStart;

    @FXML
    private TextField monEnd;

    @FXML
    private TextField tueStart;

    @FXML
    private TextField tueEnd;

    @FXML
    private TextField wenStart;

    @FXML
    private TextField wenEnd;

    @FXML
    private TextField thuStart;

    @FXML
    private TextField thuEnd;

    @FXML
    private TextField friStart;

    @FXML
    private TextField friEnd;

    @FXML
    private TextField satStart;

    @FXML
    private TextField satEnd;

    @FXML
    private TextField sunStart;

    @FXML
    private TextField sunEnd;

    @FXML
    private TextField[] schedulesInput;

    @FXML
    private Text warningTitle;

    @FXML
    private Text warningDate;

    @FXML
    private Text warningType;

    @FXML
    private Text warningLesson;

    @FXML
    private Text warningSpace;

    @FXML
    private Text successfulSaving;

    public AdminEditLessonsController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    private boolean fixTime(TextField field) {
        String text = field.getText();
        if (text != null && !text.isEmpty()) {
            int hour;
            int minute;
            if (text.matches("\\d\\d\\D\\d\\d")) {
                hour = Integer.parseInt(text.substring(0, 2));
                minute = Integer.parseInt(text.substring(3, 5));
                if (separator == null) {
                    separator = String.valueOf(text.charAt(2));
                    if (separator.equals(".")) separator = "\\.";
                } else field.setText(hour + (separator.equals("\\.") ? "." : separator) + minute);
            } else if (text.matches("\\d\\D\\d\\d")) {
                hour = Integer.parseInt(text.substring(0, 1));
                minute = Integer.parseInt(text.substring(2, 4));
                if (separator == null) {
                    separator = String.valueOf(text.charAt(1));
                    if (separator.equals(".")) separator = "\\.";
                } else field.setText(hour + (separator.equals("\\.") ? "." : separator) + minute);
            } else if (text.matches("\\d\\d\\D\\d")) {
                hour = Integer.parseInt(text.substring(0, 2));
                minute = Integer.parseInt(text.substring(3, 4));
                if (separator == null) {
                    separator = String.valueOf(text.charAt(2));
                    if (separator.equals(".")) separator = "\\.";
                } else field.setText(hour + (separator.equals("\\.") ? "." : separator) + minute);
            } else if (text.matches("\\d\\D\\d")) {
                hour = Integer.parseInt(text.substring(0, 1));
                minute = Integer.parseInt(text.substring(2, 3));
                if (separator == null) {
                    separator = String.valueOf(text.charAt(1));
                    if (separator.equals(".")) separator = "\\.";
                } else field.setText(hour + (separator.equals("\\.") ? "." : separator) + minute);
            } else {
                warningDate.setText("Введите корректное время!");
                warningDate.setVisible(true);
                return false;
            }
            if (hour < 24 && minute < 60 && 0 < hour && 0 <= minute) {
                return true;
            }
            warningDate.setText("Введите корректное время!");
            warningDate.setVisible(true);
            return false;
        }
        return true;
    }

    @FXML
    void checkMonStart() {
        String text = monStart.getText();
        monEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkTueStart() {
        String text = tueStart.getText();
        tueEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkWenStart() {
        String text = wenStart.getText();
        wenEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkThuStart() {
        String text = thuStart.getText();
        thuEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkFriStart() {
        String text = friStart.getText();
        friEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkSatStart() {
        String text = satStart.getText();
        satEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void checkSunStart() {
        String text = sunStart.getText();
        sunEnd.setDisable(text == null || text.isEmpty());
    }

    @FXML
    void cleanForm() {
        hideWarnings();
        titleInput.setText(null);
        teacherBox.getSelectionModel().clearSelection();
        dateInput.setValue(null);
        typeTable.getSelectionModel().clearSelection();
        spacesTable.getSelectionModel().clearSelection();
        lessonsTable.getSelectionModel().clearSelection();
        monStart.setText(null);
        monEnd.setText(null);
        monEnd.setDisable(true);
        tueStart.setText(null);
        tueEnd.setText(null);
        tueEnd.setDisable(true);
        wenStart.setText(null);
        wenEnd.setText(null);
        wenEnd.setDisable(true);
        thuStart.setText(null);
        thuEnd.setText(null);
        thuEnd.setDisable(true);
        friStart.setText(null);
        friEnd.setText(null);
        friEnd.setDisable(true);
        satStart.setText(null);
        satEnd.setText(null);
        satEnd.setDisable(true);
        sunStart.setText(null);
        sunEnd.setText(null);
        sunEnd.setDisable(true);
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Lesson> selectionModel = lessonsTable.getSelectionModel();
        ObservableList<Lesson> lessonsList = selectionModel.getSelectedItems();

        if (lessonsList == null || lessonsList.isEmpty()) {
            warningLesson.setText("Нужно выбрать хотя бы один кружок!");
            warningLesson.setVisible(true);
            return;
        }
        lessonsList.forEach(DataBase::removeLesson);
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<Lesson> selectionModel = lessonsTable.getSelectionModel();
        ObservableList<Lesson> selectedLessons = selectionModel.getSelectedItems();
        if (selectedLessons == null || selectedLessons.isEmpty()) {
            warningLesson.setText("Нужно выбрать кружок!");
            warningLesson.setVisible(true);
            return;
        }
        if (selectedLessons.size() == 1) {
            Lesson lesson = selectedLessons.get(0);
            titleInput.setText(lesson.getTitle());
            teacherBox.setValue(lesson.getTeacher());
            Calendar calendar = Calendar.getInstance(Locale.UK);
            calendar.setTime(new Date(lesson.getTimeStart()));
            dateInput.setValue(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            typeTable.getSelectionModel().select(lesson.getType());
            spacesTable.getSelectionModel().select(lesson.getSpace());
            tabPane.getSelectionModel().select(spaceTab);
            Long[][] schedule = lesson.getSchedule();
            if (schedule[0][0] != -1) {
                monEnd.setDisable(false);
                monStart.setText(format.format(new Date(schedule[0][0])));
                monEnd.setText(format.format(new Date(schedule[0][1])));
            }
            if (schedule[1][0] != -1) {
                tueEnd.setDisable(false);
                tueStart.setText(format.format(new Date(schedule[1][0])));
                tueEnd.setText(format.format(new Date(schedule[1][1])));
            }
            if (schedule[2][0] != -1) {
                wenEnd.setDisable(false);
                wenStart.setText(format.format(new Date(schedule[2][0])));
                wenEnd.setText(format.format(new Date(schedule[2][1])));
            }
            if (schedule[3][0] != -1) {
                thuEnd.setDisable(false);
                thuStart.setText(format.format(new Date(schedule[3][0])));
                thuEnd.setText(format.format(new Date(schedule[3][1])));
            }
            if (schedule[4][0] != -1) {
                friEnd.setDisable(false);
                friStart.setText(format.format(new Date(schedule[4][0])));
                friEnd.setText(format.format(new Date(schedule[4][1])));
            }
            if (schedule[5][0] != -1) {
                satEnd.setDisable(false);
                satStart.setText(format.format(new Date(schedule[5][0])));
                satEnd.setText(format.format(new Date(schedule[5][1])));
            }
            if (schedule[6][0] != -1) {
                sunEnd.setDisable(false);
                sunStart.setText(format.format(new Date(schedule[6][0])));
                sunEnd.setText(format.format(new Date(schedule[6][1])));
            }
        } else {
            warningLesson.setText("Нужно выбрать один кружок!");
            warningLesson.setVisible(true);
        }
    }

    @FXML
    void hideWarnings() {
        warningTitle.setVisible(false);
        warningDate.setText(null);
        warningDate.setVisible(false);
        warningType.setVisible(false);
        warningLesson.setVisible(false);
        warningSpace.setVisible(false);
        successfulSaving.setVisible(false);
    }

    @FXML
    void save() {
        hideWarnings();
        String title = titleInput.getText();
        Teacher teacher = teacherBox.getValue();
        LocalDate date = dateInput.getValue();
        Calendar timeToStart = null;
        TableView.TableViewSelectionModel<LessonType> selectedTypes = typeTable.getSelectionModel();
        ObservableList<LessonType> lessonTypeList = selectedTypes.getSelectedItems();
        TableView.TableViewSelectionModel<Space> selectedSpaces = spacesTable.getSelectionModel();
        ObservableList<Space> lessonSpaceList = selectedSpaces.getSelectedItems();
        String[] scheduleTimes = new String[]{
                monStart.getText(), monEnd.getText(),
                tueStart.getText(), tueEnd.getText(),
                wenStart.getText(), wenEnd.getText(),
                thuStart.getText(), thuEnd.getText(),
                friStart.getText(), friEnd.getText(),
                satStart.getText(), satEnd.getText(),
                sunStart.getText(), sunEnd.getText()
        };

        boolean flag = true;
        if (title == null || title.isEmpty()) {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (teacher == null) {
//            warningTeacher.setVisible(true);
            flag = false;
        }
        if (lessonTypeList == null || lessonTypeList.isEmpty()) {
            warningType.setVisible(true);
            flag = false;
        }
        if (lessonSpaceList == null || lessonSpaceList.isEmpty()) {
            tabPane.getSelectionModel().select(spaceTab);
            warningSpace.setVisible(true);
            flag = false;
        }
        try {
            if (date.toString().split("-").length != 3) {
                warningDate.setText("Дату нужно ввести в другом формате");
                warningDate.setVisible(true);
                flag = false;
            }

            Calendar.Builder dateBuilder = new Calendar.Builder();
            String[] dateSet = date.toString().split("-");
            int year = Integer.parseInt(dateSet[0]);
            int month = Integer.parseInt(dateSet[1]);
            int day = Integer.parseInt(dateSet[2]);
            dateBuilder.setDate(year, month - 1, day);
            timeToStart = dateBuilder.build();
        } catch (NullPointerException ex) {
            warningDate.setText("Что-то пошло не так, попробуйте снова!");
            warningDate.setVisible(true);
            flag = false;
        }
        if (Arrays.stream(scheduleTimes).allMatch(String::isEmpty)) {
            warningDate.setText("Нужно добавить хотя бы одно занятие в неделю!");
            warningDate.setVisible(true);
            flag = false;
        }
        for (TextField field : schedulesInput) {
            if (!fixTime(field)) return;
        }

        Long[][] schedule = new Long[7][2];
        Calendar cal = Calendar.getInstance();
        for (int i = 0, j = 0; i < scheduleTimes.length; i += 2, j++) {
            if (scheduleTimes[i].isEmpty()) {
                schedule[j][0] = -1L;
                schedule[j][1] = -1L;
            } else {
                if (!scheduleTimes[i + 1].isEmpty()) {
                    String[] time = scheduleTimes[i].split(separator);
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                    long startTime = cal.getTimeInMillis();


                    time = scheduleTimes[i + 1].split(separator);
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                    long endTime = cal.getTimeInMillis();
                    if (startTime < endTime) {
                        schedule[j][1] = startTime;
                        schedule[j][0] = endTime;
                    } else {
                        warningDate.setText("Время начала не может быть позже времени окончания!");
                        warningDate.setVisible(true);
                        flag = false;
                    }
                } else {
                    warningDate.setText("Нужно ввести время окончания!");
                    warningDate.setVisible(true);
                    flag = false;
                }
            }
        }
        if (!flag) return;
        DataBase.addLesson(title, timeToStart.getTimeInMillis(), lessonTypeList.get(0), lessonSpaceList.get(0),
                teacher, schedule);
        successfulSaving.setVisible(true);
    }

    @FXML
    void switchToEditLessonTypes() {
        applicationService.createNewWindow("editType", new EditLessonTypeController(applicationService),
                "Новый вид кружков", 505, 490);
    }

    @FXML
    void switchToEditSpaces() {
        applicationService.changeRootStage("createNewSpace", new AdminCreateNewSpaceController(applicationService));
    }

    @FXML
    void switchToPrimary() {
        applicationService.changeRootStage("adminDesktop", new AdminDesktopController(applicationService));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        schedulesInput = new TextField[]{monStart, monEnd, tueStart, tueEnd, wenStart, wenEnd, thuStart, thuEnd,
                friStart, friEnd, satStart, satEnd, sunStart, sunEnd};
        teacherBox.setConverter(new StringConverter<Teacher>() {
            @Override
            public String toString(Teacher object) {
                return object.getLastName() + " " + object.getFirstName() + " " + object.getPatronymic();
            }

            @Override
            public Teacher fromString(String string) {
                return null;
            }
        });
        FilteredList<Teacher> teachersList = new FilteredList<>(Teacher.objectsList);
        teacherBox.setItems(teachersList);

        type.setCellValueFactory(new PropertyValueFactory<>("name"));
        FilteredList<LessonType> typeList = new FilteredList<>(LessonType.objectsList);
        typeTable.setItems(typeList);

        spaceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        spaceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
//        spaceTypeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        FilteredList<Space> spaceList = new FilteredList<>(Space.objectsList, p -> p.getType().equals("lesson"));
        spacesTable.setItems(spaceList);

        lessonColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//        lessonDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        spaceColumn.setCellValueFactory(cell -> cell.getValue().getSpace().nameProperty());
        startColumn.setCellValueFactory(cell -> cell.getValue().stringTimeProperty());
        typeColumn.setCellValueFactory(cell -> cell.getValue().getType().nameProperty());
        FilteredList<Lesson> lessonList = new FilteredList<>(Lesson.objectsList, p -> true);
        lessonsTable.setItems(lessonList);

        DataBase.loadEmployeeList("teacher");
        DataBase.loadLessonTypesList();
        DataBase.loadSpacesList("lesson");
        DataBase.loadLesson();
    }
}
