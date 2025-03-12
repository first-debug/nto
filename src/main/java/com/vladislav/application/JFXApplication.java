package com.vladislav.application;

import com.vladislav.application.events.StartApplicationEvent;
import com.vladislav.infrastructure.DataBase;
import com.vladislav.presentation.servicesImpl.WindowServiceImpl;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JFXApplication extends javafx.application.Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {

        ApplicationContextInitializer<GenericApplicationContext> initializer =
               applicationContext -> {
                        applicationContext.registerBean(javafx.application.Application.class, () -> JFXApplication.this);
                        applicationContext.registerBean(DataBase.class);
                        applicationContext.registerBean(WindowServiceImpl.class);
                };

        this.context = new SpringApplicationBuilder()
                .sources(Launcher.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(@Autowired Stage primaryWindowStage) {
//        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        context.getBeanFactory().registerSingleton("primaryWindowStage", primaryWindowStage);
        Platform.runLater(() ->
                context.getBeanFactory().registerSingleton("secondWindowStage", new Stage()));
        context.publishEvent(new StartApplicationEvent(primaryWindowStage));
    }

    @Override
    public void stop() {
        if (!context.isActive()) {
            context.close();
        }
        Platform.exit();
    }
}