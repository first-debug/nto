package com.vladislav;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.vladislav.models.DataBase;
import java.lang.IllegalStateException;
import java.util.Objects;

public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static DataBase dataBase;

	public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            dataBase = new DataBase();
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
            scene = new Scene(loadFXML("primary"), 780, 540);
            App.stage = stage;
            stage.setScene(scene);
            stage.setTitle("Мероприятия Культурного центра");
            stage.getIcons().add(icon);
            stage.show();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        try 
        {
            URL fxmlFile = App.class.getResource(fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            fxmlLoader.getController();
            return fxmlLoader.load(); 
        } catch (IllegalStateException ex) {
            System.out.println("A nonexistent FXML-file is specified: " + fxml);
            return null;
        }   
    }
    
    protected static Scene getScene() {
		return scene;
	}

    public static void changeWindowsSize(int width, int height)
    {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public static void exit()
    {
        dataBase.closeConnection();
        Platform.exit();
    }
}
