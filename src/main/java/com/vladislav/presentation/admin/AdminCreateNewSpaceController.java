package com.vladislav.presentation.admin;

import com.vladislav.presentation.WindowService;
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

    private final Logger logger = LoggerFactory.getLogger(AdminCreateNewSpaceController.class);

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
    @FXML
    private Tab seatsTab;

    private GraphicsContext gc;
    private int seatsPlaneOffsetWidth;
    private int seatsPlaneOffsetHeight;
    private int[][] primarySeatsArray;
    private int sizeOfRec;
    private boolean mouseIsPressedSeatsPlane;
    private int[] seatsAreaStartCoords;
    private int[] seatsAreaPreviousCoords;
    private int typeFillSeats;

    public AdminCreateNewSpaceController(@Autowired WindowService windowService) {
        super(windowService);
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

    private Task<Void> drawSeats(int[][] seats) {
        return new Task<>() {
            @Override
            protected Void call(){
                int offsetHeight, offsetWidth;
                if (seats.length < seats[0].length && seats.length / seatsPlane.getHeight() < seats[0].length / seatsPlane.getWidth()) {
                    sizeOfRec = (int)seatsPlane.getWidth() / (seats[0].length);
                } else {
                    sizeOfRec = (int)seatsPlane.getHeight() / (seats.length);
                }
                offsetWidth = ((int)seatsPlane.getWidth() - (sizeOfRec * seats[0].length)) / 2;
                offsetHeight = ((int)seatsPlane.getHeight() - (sizeOfRec * seats.length)) / 2;
                seatsPlaneOffsetWidth = offsetWidth;
                seatsPlaneOffsetHeight = offsetHeight;

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
                        int status = seats[i][j];
                        switch (status) {
                            case 1:
                                gc.setFill(Color.CYAN);
                                break;
                            case 2:
                                gc.setFill(Color.CORAL);
                                break;
                            default:
                                gc.setFill(Color.rgb(192, 192, 192));
                                break;
                        }
                        gc.fillRect(j * sizeOfRec + offsetWidth, i * sizeOfRec + offsetHeight,
                                    sizeOfRec, sizeOfRec);
                        gc.strokeRect(j * sizeOfRec + offsetWidth, i * sizeOfRec + offsetHeight,
                                    sizeOfRec, sizeOfRec);
                    }
                }
                return null;
            }
        };
    }

    private void fillSeat(int x, int y) {
        if (primarySeatsArray[y][x] == 1)
            gc.setFill(Color.CYAN);
        else if (primarySeatsArray[y][x] == 0)
            gc.setFill(Color.rgb(192, 192, 192));
        gc.setStroke(Color.BLACK);
        gc.fillRect(x * sizeOfRec + seatsPlaneOffsetWidth,
                y * sizeOfRec + seatsPlaneOffsetHeight, sizeOfRec, sizeOfRec);
        gc.strokeRect(x * sizeOfRec + seatsPlaneOffsetWidth,
                y * sizeOfRec + seatsPlaneOffsetHeight, sizeOfRec, sizeOfRec);
    }

    private int[] getSeatCoordinates(double x, double y) {
        int[] res = new int[]{0, 0};
        if ((x - seatsPlaneOffsetWidth) / sizeOfRec < 0.0D
                || x > (seatsPlane.getWidth() - seatsPlaneOffsetWidth))
            res[0] = -1;
        else
            res[0] = (int)(x - seatsPlaneOffsetWidth) / sizeOfRec;
        if ((y - seatsPlaneOffsetHeight) / sizeOfRec < 0.0D
                || y > (seatsPlane.getHeight() - seatsPlaneOffsetHeight))
            res[1] = -1;
        else
            res[1] = (int)(y - seatsPlaneOffsetHeight) / sizeOfRec;
        return res;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = seatsPlane.getGraphicsContext2D();
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
        FilteredList<Space> filteredSpacesList = new FilteredList<>(Space.objectsList, p ->
                !DataBase.getOccupiedSpaces().contains(p.getId()));
        spacesTable.setItems(filteredSpacesList);

        mouseIsPressedSeatsPlane = false;
        seatsAreaStartCoords = new int[] {-1, -1};
        primarySeatsArray = new int[20][28];
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 28; j++)
                primarySeatsArray[i][j] = 0;
        new Thread(drawSeats(primarySeatsArray)).start();
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
        titleInput.setText("");
        typeInput.getSelectionModel().clearSelection();
        descriptionInput.setText("");
        areaInput.setText("");
        capacityInput.setText("");
        onlyOneEvent.setSelected(true);
        showPartAreaInputs(false);
        oneOrTwo.setSelected(false);
        spacesTable.getSelectionModel().clearSelection();
        hideWarnings();
        primarySeatsArray = new int[20][28];
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 28; j++)
                primarySeatsArray[i][j] = 0;
        new Thread(drawSeats(primarySeatsArray)).start();
        mouseIsPressedSeatsPlane = false;
        seatsAreaStartCoords = new int[]{-1, -1};
        seatsAreaPreviousCoords = null;
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
                primarySeatsArray = space.getSeats();
                new Thread(drawSeats(primarySeatsArray)).start();
                seatsTab.setDisable(false);
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
            else
                seatsTab.setDisable(true);
        }
    }

    @FXML
    void hideWarnings() {
        try {
            successfulSaving.setVisible(false);
            warningSpace.setVisible(false);
            warningDescription.setVisible(false);
            warningTitle.setVisible(false);
            warningType.setVisible(false);
            seatsTab.setDisable(false);
        } catch (NullPointerException ex) {
            logger.warn(ex.getMessage());
        }
    }

    @FXML
    void save() {
        hideWarnings();
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        String areaStr = areaInput.getText();
        String capacity = capacityInput.getText();
        boolean isOnlyOne = onlyOneEvent.isSelected();
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
                capacity.isEmpty() ? 0 : Integer.parseInt(capacity), !isOnlyOne,
                isOnlyOne ? new Integer[]{area, -1} : new Integer[]{
                        firstArea.getText().isEmpty() ? area : Integer.parseInt(firstArea.getText()),
                        secondArea.getText().isEmpty() ? 0 : Integer.parseInt(secondArea.getText())},
                type, primarySeatsArray);
        successfulSaving.setVisible(true);
    }

    @FXML
    void switchToPrimary() {
        windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
    }

    @FXML
    private void seatsPlaneMousePressed(MouseEvent event) {
        int[] coordinates = getSeatCoordinates(event.getX(), event.getY());
        typeFillSeats = primarySeatsArray[coordinates[1]][coordinates[0]] == 0 ? 1 : 0;
        if (coordinates[0] == -1 || coordinates[1] == -1)
            return;
        if (primarySeatsArray[coordinates[1]][coordinates[0]] == 0)
            primarySeatsArray[coordinates[1]][coordinates[0]] = 1;
        else
            primarySeatsArray[coordinates[1]][coordinates[0]] = 0;
        fillSeat(coordinates[0], coordinates[1]);
        seatsAreaStartCoords = coordinates.clone();
        mouseIsPressedSeatsPlane = true;
    }

    @FXML
    private void seatsPlaneMouseMoved(MouseEvent event) {
        int[] coordinates = getSeatCoordinates(event.getX(), event.getY());
        if (!mouseIsPressedSeatsPlane || Arrays.equals(seatsAreaPreviousCoords, coordinates))
            return;
        if (coordinates[0] == -1 || coordinates[1] == -1)
            return;
        // x to right, y to down
        if (coordinates[0] >= seatsAreaStartCoords[0] && coordinates[1] >= seatsAreaStartCoords[1])
            for (int i = 0; i <= coordinates[1] - seatsAreaStartCoords[1]; i++)
                for (int j = 0; j <= coordinates[0] - seatsAreaStartCoords[0]; j++) {
                    primarySeatsArray[seatsAreaStartCoords[1] + i][seatsAreaStartCoords[0] + j] = typeFillSeats;
                    fillSeat(seatsAreaStartCoords[0] + j, seatsAreaStartCoords[1] + i);
                }
        // x to left, y to down
        else if (coordinates[0] < seatsAreaStartCoords[0] && coordinates[1] >= seatsAreaStartCoords[1])
            for (int i = 0; i <= coordinates[1] - seatsAreaStartCoords[1]; i++)
                for (int j = 0; j <= seatsAreaStartCoords[0] - coordinates[0]; j++) {
                    primarySeatsArray[seatsAreaStartCoords[1] + i][coordinates[0] + j] = typeFillSeats;
                    fillSeat(coordinates[0] + j, seatsAreaStartCoords[1] + i);
                }
        // x to right, y to up
        else if (coordinates[0] >= seatsAreaStartCoords[0] && coordinates[1] < seatsAreaStartCoords[1])
            for (int i = 0; i <= seatsAreaStartCoords[1] - coordinates[1]; i++)
                for (int j = 0; j <= coordinates[0] - seatsAreaStartCoords[0]; j++) {
                    primarySeatsArray[coordinates[1] + i][seatsAreaStartCoords[0] + j] = typeFillSeats;
                    fillSeat(seatsAreaStartCoords[0] + j, coordinates[1] + i);
                }
        else
            for (int i = 0; i <= seatsAreaStartCoords[1] - coordinates[1]; i++)
                for (int j = 0; j <= seatsAreaStartCoords[0] - coordinates[0]; j++) {
                    primarySeatsArray[coordinates[1] + i][coordinates[0] + j] = typeFillSeats;
                    fillSeat(coordinates[0] + j, coordinates[1] + i);
                }
        seatsAreaPreviousCoords = coordinates.clone();
    }

    @FXML
    private void seatsPlaneMouseReleased(MouseEvent event) {
        mouseIsPressedSeatsPlane = false;
    }
}
