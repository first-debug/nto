package com.vladislav.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
			BufferedReader doc = new BufferedReader(new FileReader("documentation.txt", Charset.forName("windows-1251")));
            textArea.setText(doc.lines().reduce((x, y) -> x + '\n' + y).get());              
            doc.close();
		} 
        catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

	}

}
