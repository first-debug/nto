package com.vladislav.controllers;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import com.vladislav.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class AboutController extends Controller implements Initializable {

    @FXML
    private Text textArea;

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
            App.logger.error(ex.getMessage());
        }

    }

}
