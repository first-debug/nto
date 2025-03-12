package com.vladislav.presentation.primary;

import com.vladislav.presentation.services.WindowService;
import com.vladislav.presentation.EventTablesController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

public class EntertainmentEventsController extends EventTablesController implements Initializable {

    @FXML
    private Button goBackButton;

    public EntertainmentEventsController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        createEventsTable(true, "Развлечения");
    }
}