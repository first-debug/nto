package com.vladislav.application;

import com.vladislav.application.events.CreateSecondWindowEvent;
import com.vladislav.presentation.Controller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class ApplicationService {
    private final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    private final ConfigurableApplicationContext applicationContext;

    public ApplicationService(@Autowired ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void stopApplication() {
        applicationContext.close();
        Platform.exit();
    }

    public void createNewWindow(String fxml, Controller controller, String title, Integer width, Integer height) {
        try {
            applicationContext.publishEvent(new CreateSecondWindowEvent(fxml, controller, title, width, height));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeSecondWindow() {
        ((Stage) applicationContext.getBean("secondWindowStage")).hide();
    }

    public void changeRootStage(String fxml, Controller controller) {
        ((Stage) applicationContext.getBean("primaryWindowStage"))
                .getScene()
                .setRoot(loadFXML(fxml, controller));
    }

    public Parent loadFXML(String fxml, Controller controller) {
        try {
            URL fxmlFile;
            fxmlFile = JFXApplication.class.getResource("UIMarkups/" + fxml + ".fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
            fxmlLoader.setController(controller);
            return fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalStateException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }
}
