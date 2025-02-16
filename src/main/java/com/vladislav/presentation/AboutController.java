package com.vladislav.presentation;

import com.vladislav.application.ApplicationService;
import com.vladislav.application.JFXApplication;
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

    public AboutController(@Autowired ApplicationService applicationService) {
        super(applicationService);
    }

    @FXML
    private void close() {
        applicationService.closeSecondWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL doc = JFXApplication.class.getResource("documentation.txt");
            assert doc != null;
            BufferedReader text = new BufferedReader(new InputStreamReader((InputStream) doc.getContent()));
            text.lines().reduce((x, y) -> x + '\n' + y).ifPresent(t -> textArea.setText(t));
            text.close();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }

    }

}
