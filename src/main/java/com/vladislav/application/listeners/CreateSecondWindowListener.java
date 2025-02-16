package com.vladislav.application.listeners;

import com.vladislav.application.ApplicationService;
import com.vladislav.application.events.CreateSecondWindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Objects;

@Component
@Lazy
public class CreateSecondWindowListener implements ApplicationListener<CreateSecondWindowEvent> {

    private final Logger logger = LoggerFactory.getLogger(CreateSecondWindowListener.class);

    private final ApplicationService applicationService;
    private final Image applicationIcon;
    private final Stage stage;


    public CreateSecondWindowListener(@Value("${ui.icon}") Resource appIconPath,
                                      @Autowired ApplicationService applicationService,
                                      @Autowired @Qualifier("secondWindowStage") Stage secondWindowStage) {
        try {
            applicationIcon = new Image(Objects.requireNonNull(appIconPath.getInputStream()));
            this.applicationService = applicationService;
            this.stage = secondWindowStage;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onApplicationEvent(@NonNull CreateSecondWindowEvent createEvent) {
        try {
            Parent parent = applicationService.loadFXML(createEvent.getFxml(), createEvent.getController());
            stage.getIcons().add(applicationIcon);
            Scene scene = new Scene(parent, createEvent.getWidth(), createEvent.getHeight());
            stage.setMinWidth(500);
            stage.setMinHeight(300);
            stage.setScene(scene);
            System.setProperty("java.awt.headless", "false");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            stage.setMaxWidth(screenSize.width);
            stage.setMaxHeight(screenSize.height);
            stage.setTitle(createEvent.getTitle());
            stage.getIcons().add(applicationIcon);
            stage.setOnCloseRequest(event -> {
                try {
                    applicationService.closeSecondWindow();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
