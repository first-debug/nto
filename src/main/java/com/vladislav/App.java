package com.vladislav;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import com.vladislav.models.DataBase;
import java.lang.IllegalStateException;

public class App extends Application {

    private static Scene scene;
    private static DataBase dataBase;

	public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        dataBase = new DataBase();
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Мероприятия Культурного центра");
        stage.show();
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

    public static void exit()
    {
        dataBase.closeConnection();
        Platform.exit();
    }
}
