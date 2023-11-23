package com.vladislav.controllers;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.ResourceBundle;

import com.vladislav.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class AboutController extends Controller implements Initializable {

    @FXML
    private Text textArea;

    @FXML
    private void switchToPrimary(ActionEvent event) throws IOException {
        App.setRoot("primary");
    }

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	    try 
        {
            URL doc = App.class.getResource("documentation.txt");
            assert doc != null;
            BufferedReader text = new BufferedReader(new InputStreamReader((InputStream) doc.getContent()));
            text.lines().reduce((x, y) -> x + '\n' + y).ifPresent(t -> textArea.setText(t));
            text.close();
		} catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
