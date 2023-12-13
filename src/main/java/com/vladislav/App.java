package com.vladislav;

import com.vladislav.controllers.Controller;
import com.vladislav.controllers.primary.PrimaryController;
import com.vladislav.models.DataBase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class App extends Application {

    public static Logger logger;
    public static SimpleDateFormat dateFormat;
    private static Scene scene;
    private static Stage newStageWindow;
    private static Controller newStageController;
    private static Stage stage;
    private static DataBase dataBase;
    private static Image appIcon;

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(App.class);
        launch();
    }

    public static void setRoot(String fxml, Controller controller) {
        scene.setRoot(loadFXML(fxml, controller));
    }

    public static void newWindow(String fxml, Controller controller, String title, Integer width, Integer height) {
        if (newStageWindow == null) {
            Parent parent = loadFXML(fxml, controller);
            newStageWindow = new Stage();
            newStageController = controller;
            newStageWindow.setTitle(title);
            newStageWindow.setScene(new Scene(parent, width, height));
            newStageWindow.getIcons().add(appIcon);
            newStageWindow.show();
        } else if (newStageController.getClass().equals(controller.getClass())) {
            newStageWindow.hide();
            newStageWindow.show();
        } else {
            Parent parent = loadFXML(fxml, controller);
            newStageWindow = new Stage();
            newStageController = controller;
            newStageWindow.setTitle(title);
            newStageWindow.setScene(new Scene(parent, width, height));
            newStageWindow.getIcons().add(appIcon);
            newStageWindow.show();
        }
    }

    public static void newWindow(String fxml, Controller controller, String title) {
        newWindow(fxml, controller, title, 720, 540);
    }

    public static void closeSecondWindow() {
        newStageWindow.hide();
    }

    private static Parent loadFXML(String fxml, Controller controller) {
        try {
            URL fxmlFile = App.class.getResource("UIMarkups/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            fxmlLoader.setController(controller);
            return fxmlLoader.load();
        } catch (IOException ex) {
            logger.error("A nonexistent FXML-file is specified: " + fxml);
            ex.printStackTrace();
            return null;
        } catch (IllegalStateException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

    public static void changeWindowSize(int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void exit() {
        dataBase.closeConnection();
        if (newStageWindow != null) closeSecondWindow();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) {
        try {
            dataBase = new DataBase();
            dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
            appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            scene = new Scene(loadFXML("primary", new PrimaryController()), 1240, 650);
            stage.setMinWidth(960);
            stage.setMinHeight(600);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            stage.setMaxWidth(screenSize.width);
            stage.setMaxHeight(screenSize.height);
            App.stage = stage;
            stage.setScene(scene);
            stage.setTitle("Мероприятия Культурного центра");
            stage.getIcons().add(appIcon);
            stage.setOnCloseRequest(event -> exit());
            stage.show();
        } catch (Exception ex) {
            logger.error(54 + " " + ex.getMessage());
        }
    }
}
