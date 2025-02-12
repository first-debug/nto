package com.vladislav;

import com.vladislav.models.DataBase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class App extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
               applicationContext -> {
                        applicationContext.registerBean(Application.class, () -> App.this);
                        applicationContext.registerBean(DataBase.class);
                };

        this.context = new SpringApplicationBuilder()
                .sources(MainLauncher.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) {
        context.publishEvent(new StartApplicationEvent(stage));
    }

    @Override
    public void stop() throws Exception {
        context.close();
//        dataBase.closeConnection();
//        if (newStageWindow != null) closeSecondWindow();
        Platform.exit();
    }

//    public static void setRoot(String fxml, Controller controller) {
//        scene.setRoot(loadFXML(fxml, controller));
//    }

//    public static void newWindow(String fxml, Controller controller, String title, Integer width, Integer height) {
//        if (newStageWindow == null) {
//            Parent parent = loadFXML(fxml, controller);
//            newStageWindow = new Stage();
//            newStageWindow.setMinWidth(600);
//            newStageWindow.setMinHeight(400);
//            newStageController = controller;
//            newStageWindow.setTitle(title);
//            assert parent != null;
//            newStageWindow.setScene(new Scene(parent, width, height));
//            newStageWindow.getIcons().add(appIcon);
//            newStageWindow.show();
//        } else if (newStageController.getClass().equals(controller.getClass())) {
//            newStageWindow.hide();
//            newStageWindow.show();
//        } else {
//            Parent parent = loadFXML(fxml, controller);
//            newStageWindow = new Stage();
//            newStageWindow.setMinWidth(600);
//            newStageWindow.setMinHeight(400);
//            newStageController = controller;
//            newStageWindow.setTitle(title);
//            assert parent != null;
//            newStageWindow.setScene(new Scene(parent, width, height));
//            newStageWindow.getIcons().add(appIcon);
//            newStageWindow.show();
//        }
//    }

//    public static void closeSecondWindow() {
//        newStageWindow.hide();
//    }

//    private static Parent loadFXML(String fxml, Controller controller) {
//        try {
//            URL fxmlFile = App.class.getResource("UIMarkups/" + fxml + ".fxml");
//            FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
//            fxmlLoader.setController(controller);
//            return fxmlLoader.load();
//        } catch (IOException ex) {
//            logger.error("A nonexistent FXML-file is specified: " + fxml);
//            ex.printStackTrace();
//            return null;
//        } catch (IllegalStateException ex) {
//            logger.error(ex.getMessage());
//            return null;
//        }
//    }

}

class StartApplicationEvent extends ApplicationEvent {

    public Stage getStage() {
        return (Stage) getSource();
    }

    public StartApplicationEvent(Object source) {
        super(source);
    }
}
