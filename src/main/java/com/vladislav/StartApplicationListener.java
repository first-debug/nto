package com.vladislav;

import com.vladislav.controllers.Controller;
import com.vladislav.controllers.primary.PrimaryController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Component
public class StartApplicationListener implements ApplicationListener<StartApplicationEvent> {

    private final Logger logger = LoggerFactory.getLogger("application");
    private final String applicationTitle;
    private final Image applicationIcon;

    public StartApplicationListener(@Value("${ui.icon}") Resource appIconPath, @Value("${ui.title}") String title) {
        try {
            applicationIcon = new Image(Objects.requireNonNull(appIconPath.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicationTitle = title;
    }

    @Override
    public void onApplicationEvent(StartApplicationEvent startApplicationEvent) {
        try {
            Stage stage = startApplicationEvent.getStage();
            Parent parent = loadFXML("primary", new PrimaryController());
            assert parent != null;
            Scene scene = new Scene(parent, 1240, 650);
            stage.setMinWidth(960);
            stage.setMinHeight(600);
            System.setProperty("java.awt.headless", "false");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            stage.setMaxWidth(screenSize.width);
            stage.setMaxHeight(screenSize.height);
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.getIcons().add(applicationIcon);
            stage.setOnCloseRequest(event -> {
                try {
//                    stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    private Parent loadFXML(String fxml, Controller controller) {
        try {
            URL fxmlFile = App.class.getResource("UIMarkups/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            fxmlLoader.setController(controller);
            return fxmlLoader.load();
        } catch (IOException ex) {
            logger.error("A nonexistent FXML-file is specified: {}", fxml);
            return null;
        } catch (IllegalStateException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }
}
