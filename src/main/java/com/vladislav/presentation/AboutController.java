package com.vladislav.presentation;

import com.vladislav.application.JFXApplication;
import com.vladislav.presentation.services.WindowService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AboutController extends Controller implements Initializable {

    private final Logger logger = LoggerFactory.getLogger(AboutController.class);

    @FXML
    private Text textArea;

    public AboutController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    private void close() {
        windowService.closeSecondWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL doc = JFXApplication.class.getResource("documentation.txt");
            BufferedReader text = new BufferedReader(new InputStreamReader((InputStream) doc.getContent()));
            text.lines().reduce((x, y) -> x + '\n' + y).ifPresent(t -> textArea.setText(t));
            text.close();
        } catch (IOException | NullPointerException ex) {
            logger.error(ex.getMessage());
        }

    }

}
