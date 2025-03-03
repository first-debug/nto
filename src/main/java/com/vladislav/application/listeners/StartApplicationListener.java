package com.vladislav.application.listeners;

import com.vladislav.presentation.WindowService;
import com.vladislav.application.events.StartApplicationEvent;
import com.vladislav.presentation.primary.PrimaryController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

@Component
public class StartApplicationListener implements ApplicationListener<StartApplicationEvent> {

    private final Logger logger = LoggerFactory.getLogger("application");

    private final String applicationTitle;
    private final Image applicationIcon;
    private final ConfigurableApplicationContext context;
    public final WindowService windowService;

    public StartApplicationListener(@Value("${ui.icon}") Resource appIconPath, @Value("${ui.title}") String title,
                                    @Autowired ConfigurableApplicationContext context) {
        try {
            applicationIcon = new Image(Objects.requireNonNull(appIconPath.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicationTitle = title;
        this.context = context;
        windowService = context.getBean(WindowService.class);
    }

    @Override
    public void onApplicationEvent(@NonNull StartApplicationEvent startApplicationEvent) {
        try {
            Stage stage = startApplicationEvent.getStage();
            Parent parent = windowService
                    .loadFXML("primary", new PrimaryController(context.getBean(WindowService.class)));
            Scene scene = new Scene(parent, 1240, 650);
            stage.setMinWidth(960);
            stage.setMinHeight(600);
            stage.setScene(scene);
            System.setProperty("java.awt.headless", "false");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            stage.setMaxWidth(screenSize.width);
            stage.setMaxHeight(screenSize.height);
            stage.setTitle(applicationTitle);
            stage.getIcons().add(applicationIcon);
            stage.setOnCloseRequest(event -> {
                try {
                    context.close();
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
