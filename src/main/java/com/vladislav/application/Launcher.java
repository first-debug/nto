package com.vladislav.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Launcher {
    public static void main(String[] args) {
        javafx.application.Application.launch(JFXApplication.class, args);
    }
}
