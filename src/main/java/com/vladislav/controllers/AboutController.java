package com.vladislav.controllers;

import com.vladislav.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController extends Controller implements Initializable {

    @FXML
    private Text textArea;

    @FXML
    private void close() {
//        App.closeSecondWindow();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL doc = App.class.getResource("documentation.txt");
            assert doc != null;
            BufferedReader text = new BufferedReader(new InputStreamReader((InputStream) doc.getContent()));
            text.lines().reduce((x, y) -> x + '\n' + y).ifPresent(t -> textArea.setText(t));
            text.close();
        } catch (IOException ex) {
//            App.logger.error(ex.getMessage());
        }

    }

}
