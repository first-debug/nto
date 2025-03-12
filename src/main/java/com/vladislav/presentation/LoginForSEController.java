package com.vladislav.presentation;

import com.vladislav.infrastructure.DataBase;
import com.vladislav.presentation.admin.AdminDesktopController;
import com.vladislav.presentation.services.WindowService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginForSEController extends Controller {

    @FXML
    private TextField loginInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Text warningLogin;

    @FXML
    private Text warningPassword;

    public LoginForSEController(@Autowired WindowService windowService) {
        super(windowService);
    }

    @FXML
    void hideWarnings() {
        warningLogin.setVisible(false);
        warningLogin.setText(null);

        warningPassword.setVisible(false);
        warningPassword.setText(null);
    }

    @FXML
    void cleanForm() {
        loginInput.setText(null);
        passwordInput.setText(null);
        hideWarnings();
    }

    @FXML
    void login() {
        if (loginInput.getText().equalsIgnoreCase("e"))
            windowService.changeRootStage("desktopForSE", new SEDesktopController(windowService));
        if (loginInput.getText().equalsIgnoreCase("a") || loginInput.getText().equalsIgnoreCase(""))
            windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
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
        boolean accountIsCorrect = Boolean.parseBoolean(dbAnswer[0]);
        if (!accountIsCorrect) {
            warningPassword.setText("Неверный логин или пароль!");
            warningPassword.setVisible(true);
            return;
        }
        String role = dbAnswer[1];
        if (role.equals("admin"))
            windowService.changeRootStage("adminDesktop", new AdminDesktopController(windowService));
        else if (role.equals("service"))
            windowService.changeRootStage("desktopForSE", new SEDesktopController(windowService));
    }

    @FXML
    void loginKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (loginInput.getText() == null || loginInput.getText().isEmpty()) {
                warningLogin.setVisible(true);
                return;
            }
            hideWarnings();
            passwordInput.requestFocus();
        }
    }

    @FXML
    void passwordKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (passwordInput.getText() == null || passwordInput.getText().isEmpty()) {
                warningPassword.setText("Нужно ввести пароль!");
                warningPassword.setVisible(true);
                warningLogin.setVisible(true);
                return;
            }
            hideWarnings();
            login();
        }
    }
}
