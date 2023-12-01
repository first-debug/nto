package com.vladislav;

import com.vladislav.controllers.Controller;
import com.vladislav.controllers.primary.PrimaryController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import com.vladislav.models.DataBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.IllegalStateException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class App extends Application {

    public static Logger logger;
    private static Scene scene;
    private static Stage newStageWindow;
    private static Stage stage;
    private static DataBase dataBase;
    private static Image appIcon;

    public static SimpleDateFormat format;

	public static void main(String[] args) {
        logger = LoggerFactory.getLogger(App.class);
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            dataBase = new DataBase();
            format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
            appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            scene = new Scene(loadFXML("primary", new PrimaryController()), 980, 650);
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

    public static void setRoot(String fxml, Controller controller) {
        try {
            scene.setRoot(loadFXML(fxml, controller));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void newSecondWindow(String fxml, Controller controller, String title) throws IOException {
        if (newStageWindow == null) {
            Parent parent = loadFXML(fxml, controller);
            newStageWindow = new Stage();
            newStageWindow.setTitle(title);
            newStageWindow.setScene(new Scene(parent, 600, 450));
            newStageWindow.getIcons().add(appIcon);
            newStageWindow.show();
        }
        else {
            newStageWindow.hide();
            newStageWindow.show();
        }
    }

    public static void closeSecondWindow() {
        newStageWindow.hide();
    }

    private static Parent loadFXML(String fxml, Controller controller) throws IOException {
        try 
        {
            URL fxmlFile = App.class.getResource("UIMarkups/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            fxmlLoader.setController(controller);
            return fxmlLoader.load(); 
        } catch (IllegalStateException ex) {
            logger.error("A nonexistent FXML-file is specified: " + fxml);
            return null;
        }   
    }

    public static void changeWindowSize(int width, int height)
    {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void exit()
    {
        dataBase.closeConnection();
        if (newStageWindow != null) closeSecondWindow();
        Platform.exit();
    }
}
