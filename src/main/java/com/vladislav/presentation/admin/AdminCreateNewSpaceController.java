package com.vladislav.presentation.admin;

import com.vladislav.application.ApplicationService;
import com.vladislav.presentation.AdminDesktopController;
import com.vladislav.presentation.Controller;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.domain.Space;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AdminCreateNewSpaceController extends Controller implements Initializable {

    @FXML
    private TextField titleInput;
    @FXML
    private Text warningTitle;
    @FXML
    private Text warningType;
    @FXML
    private ChoiceBox<String> typeInput;
    @FXML
    private TextArea descriptionInput;
    @FXML
    private Text warningDescription;
    @FXML
    private TextField areaInput;
    @FXML
    private TextField capacityInput;
    @FXML
    private RadioButton oneOrTwo;
    @FXML
    private RadioButton onlyOneEvent;
    @FXML
    private Text areaText1;
    @FXML
    private TextField firstArea;
    @FXML
    private Text areaText2;
    @FXML
    private Text areaText3;
    @FXML
    private TextField secondArea;
    @FXML
    private Text areaText4;
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
    private TableColumn<Space, String> typeColumn;
    @FXML
    private Text warningSpace;
    @FXML
    private Text successfulSaving;
    @FXML
    private HBox eventProperty;
    @FXML
    private Canvas seatsPlane;
    @FXML
    private Tab spacesTab;

    private int seatsPlaneOffsetWidth;
    private int seatsPlaneOffsetHeight;

    public AdminCreateNewSpaceController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    void showPartAreaInputs(boolean value) {
        String style = "-fx-fill:" + (value ? "black" : "grey");
        areaText1.setStyle(style);
        firstArea.setDisable(!value);
        areaText2.setStyle(style);
        areaText3.setStyle(style);
        secondArea.setDisable(!value);
        areaText4.setStyle(style);
    }

    @FXML
    void checkOnlyOneEvent() {
        hideWarnings();
        oneOrTwo.setSelected(false);
        showPartAreaInputs(false);
        if (!onlyOneEvent.isSelected()) onlyOneEvent.setSelected(true);
    }

    @FXML
    void checkOneOrTwo() {
        hideWarnings();
        onlyOneEvent.setSelected(false);
        showPartAreaInputs(true);
        if (!oneOrTwo.isSelected()) oneOrTwo.setSelected(true);
    }

    @FXML
    void cleanForm() {
        titleInput.setText(null);
        typeInput.getSelectionModel().clearSelection();
        descriptionInput.setText(null);
        areaInput.setText(null);
        capacityInput.setText(null);
        onlyOneEvent.setSelected(true);
        showPartAreaInputs(false);
        oneOrTwo.setSelected(false);
        spacesTable.getSelectionModel().clearSelection();
        hideWarnings();
    }

    @FXML
    void delete() {
        hideWarnings();
        TableView.TableViewSelectionModel<Space> selectionModel = spacesTable.getSelectionModel();
        ObservableList<Space> spaceList = selectionModel.getSelectedItems();
        if (spaceList == null || spaceList.isEmpty()) {
            warningSpace.setVisible(true);
            return;
        }
        spaceList.forEach(DataBase::removeSpace);
    }

    @FXML
    void edit() {
        hideWarnings();
        TableView.TableViewSelectionModel<Space> selectionModel = spacesTable.getSelectionModel();
        ObservableList<Space> selectedSpaces = selectionModel.getSelectedItems();
        if (selectedSpaces == null || selectedSpaces.isEmpty()) {
            warningSpace.setVisible(true);
            return;
        }
        if (selectedSpaces.size() == 1) {
            Space space = selectedSpaces.get(0);
            titleInput.setText(space.getName());
            typeInput.getSelectionModel().select(space.getType().equals("event") ? 0 : 1);
            descriptionInput.setText(space.getDescription());
            if (space.getType().equals("event")) {
                areaInput.setText(space.getArea().toString());
                capacityInput.setText(space.getCapacity().toString());
                Integer[] partsArea = space.getPartsArea();
                if (partsArea.length != 0) {
                    onlyOneEvent.setSelected(false);
                    showPartAreaInputs(true);
                    oneOrTwo.setSelected(true);
                    firstArea.setText(partsArea[0].toString());
                    secondArea.setText(partsArea[1].toString());
                } else {
                    onlyOneEvent.setSelected(true);
                    showPartAreaInputs(false);
                    oneOrTwo.setSelected(false);
                }
            }
        }
    }

    @FXML
    void hideWarnings() {
        warningSpace.setVisible(false);
        warningDescription.setVisible(false);
        warningTitle.setVisible(false);
        warningType.setVisible(false);
    }

    @FXML
    void save() {
        hideWarnings();
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String areaStr = areaInput.getText();
        String capacity = capacityInput.getText();
        Boolean isOnlyOne = onlyOneEvent.isSelected();
        String type = typeInput.getValue();

        boolean flag = true;
        if (title == null || title.isEmpty()) {
            warningTitle.setVisible(true);
            flag = false;
        }
        if (type == null) {
            warningType.setVisible(true);
            flag = false;
        }
        if (description == null || description.isEmpty()) {
            warningDescription.setVisible(true);
            flag = false;
        }
        if (!flag) return;
        Integer area = areaStr.isEmpty() ? 0 : Integer.parseInt(areaStr);
        type = type.equals("Для мероприятий") ? "event" : "lesson";
        DataBase.addSpace(title, description, area,
                capacity.isEmpty() ? 0 : Integer.parseInt(capacity), isOnlyOne,
                isOnlyOne ? new Integer[]{area, -1} : new Integer[]{
                        firstArea.getText().isEmpty() ? area : Integer.parseInt(firstArea.getText()),
                        secondArea.getText().isEmpty() ? 0 : Integer.parseInt(secondArea.getText())},
                type, new Integer[]{});
        successfulSaving.setVisible(true);
    }

    @FXML
    void switchToPrimary() {
        applicationService.changeRootStage("adminDesktop", new AdminDesktopController(applicationService));
    }

    private Task<Void> drawSeats(Seat[][] seats) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                int size = Seat.getSizeOfRec();
                int offsetHeight, offsetWidth;
                offsetWidth = ((int)seatsPlane.getWidth() - (size * seats[0].length)) / 2;
                offsetHeight = ((int)seatsPlane.getHeight() - (size * seats.length)) / 2;
                seatsPlaneOffsetWidth = offsetWidth;
                seatsPlaneOffsetHeight = offsetHeight;

                GraphicsContext gc = seatsPlane.getGraphicsContext2D();
                gc.setFill(Color.rgb(192, 192, 192));
                gc.fillRect(0, 0, seatsPlane.getWidth(), seatsPlane.getHeight());
                gc.setStroke(Color.BLACK);
                gc.strokeRect(offsetWidth,
                        offsetHeight,
                        seatsPlane.getWidth() - offsetWidth * 2,
                        seatsPlane.getHeight() - offsetHeight * 2
                );
                for (int i = 0; i < seats.length; i++) {
                    for (int j = 0; j < seats[i].length; j++) {
                        int status = seats[i][j].getStatus();
                        switch (status) {
                            case 1:
                                gc.setFill(Color.BLUE);
                                break;
                            case 2:
                                gc.setFill(Color.RED);
                                break;
                            default:
                                gc.setFill(Color.rgb(192, 192, 192));
                                break;
                        }
                        gc.fillRect(j * size + offsetWidth, i * size + offsetHeight, size, size);
                        if (status == 1 || status == 2)
                            gc.strokeRect(j * size + offsetWidth, i * size + offsetHeight, size, size);
                    }
                }
                return null;
            }
        };
    }

    @FXML
    private void seatsPlaneClicked(MouseEvent event) {
        Logger logger = LoggerFactory.getLogger(AdminCreateNewSpaceController.class);
        double x = event.getX(), y = event.getY();
        int seatX, seatY;
        if ((x - seatsPlaneOffsetWidth) / Seat.getSizeOfRec() < 0.0D
                || x > seatsPlane.getWidth() - 2 * seatsPlaneOffsetWidth)
            seatX = -1;
        else
            seatX = (int)(x - seatsPlaneOffsetWidth) / Seat.getSizeOfRec();
        if ((y - seatsPlaneOffsetHeight) / Seat.getSizeOfRec() < 0.0D
                || y > seatsPlane.getHeight() - 2 * seatsPlaneOffsetHeight)
            seatY = -1;
        else
            seatY = (int)(y - seatsPlaneOffsetHeight) / Seat.getSizeOfRec();

        logger.info("{}, {}", seatX, seatY);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeInput.valueProperty().addListener((observable, oldValue, newValue) ->
                eventProperty.setVisible(newValue == null || newValue.equals("Для мероприятий")));
        typeInput.setItems(FXCollections.observableList(Arrays.asList("Для мероприятий", "Для кружков")));
        spaceColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        typeColumn.setCellValueFactory(cell ->
           switch (cell.getValue().getType()) {
                case "event" -> new SimpleStringProperty("Для событий");
                case "lesson" -> new SimpleStringProperty("Для кружков");
                default -> new SimpleStringProperty("error");
            }
        );
        DataBase.loadSpacesList(null);
        FilteredList<Space> filteredSpacesList = new FilteredList<>(Space.objectsList, p -> true);
        spacesTable.setItems(filteredSpacesList);

        new Thread(drawSeats(Seat.getSeats(filteredSpacesList.get(0).getSeats(),
                seatsPlane.getHeight(),
                seatsPlane.getWidth()))).start();
    }
}

class Seat {
    private static int sizeOfRec;
    // 0 - места нет, 1 - место есть, 2 - место занято, 3 - переход на следующий ряд
    private int status;

    public Seat(int status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static int getSizeOfRec() {
        return sizeOfRec;
    }

    public static Seat[][] getSeats(int[][] input, double height, double width) {
        Seat[][] seats = new Seat[input.length][input[0].length];
        if (input.length < input[0].length) {
            sizeOfRec = (int)width / (input[0].length);
        } else {
            sizeOfRec = (int)height / (input.length);
        }
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = new Seat(input[i][j]);
            }
        }
        return seats;
    }

    @Override
    public String toString() {
        return "Seat{" + status +
                '}';
    }
}
