package com.vladislav.presentation.services;

import com.vladislav.presentation.Controller;
import javafx.scene.Parent;

public interface WindowService {
    void stopApplication();
    void createNewWindow(String fxml, Controller controller, String title, Integer width, Integer height);
    void closeSecondWindow();
    void changeRootStage(String fxml, Controller controller);
    Parent loadFXML(String fxml, Controller controller);

}
