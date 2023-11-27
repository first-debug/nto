package com.vladislav.controllers;

import com.vladislav.App;
import com.vladislav.models.DataBase;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginForSEController extends Controller{

    @FXML
    private TextField loginInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Text warningLogin;

    @FXML
    private Text warningPassword;

    @FXML
    void hideWarnings()
    {
        warningLogin.setVisible(false);
        warningLogin.setText(null);

        warningPassword.setVisible(false);
        warningPassword.setText(null);
    }

    @FXML
    void clearForm() {
        loginInput.setText(null);
        passwordInput.setText(null);
        hideWarnings();
    }

    @FXML
    void login() throws IOException {
        boolean flag = true;

        String login = loginInput.getText();
        String password = passwordInput.getText();

        if (login == null) {
            warningLogin.setText("Нужно ввести логин!");
            warningLogin.setVisible(true);
            flag = false;
        }
        if (login != null && login.isEmpty()) {
            warningLogin.setText("Нужно ввести логин!");
            warningLogin.setVisible(true);
            flag = false;
        }
        if (passwordInput == null) {
            warningPassword.setText("Нужно ввести пароль!");
            warningPassword.setVisible(true);
            flag = false;
        }
        if (password != null && password.isEmpty()) {
            warningPassword.setText("Нужно ввести пароль!");
            warningPassword.setVisible(true);
            flag = false;
        }
        if (!flag) return;

        String[] dbAnswer = DataBase.loginEmployee(login, password);
        boolean loginIsCorrect = Boolean.parseBoolean(dbAnswer[0]);
        boolean passwordIsCorrect = Boolean.parseBoolean(dbAnswer[1]);
        if (!loginIsCorrect) {
            warningLogin.setText("Логин неверный!");
            warningLogin.setVisible(true);
            return;
        }
        if (!passwordIsCorrect) {
            warningPassword.setText("Пароль неверный!");
            warningPassword.setVisible(true);
            return;
        }
        String role = dbAnswer[2];
        if (role.equals("admin")) App.setRoot("adminDesktop");
        else if (role.equals("service")) App.setRoot("desktopForSE");
    }
}
