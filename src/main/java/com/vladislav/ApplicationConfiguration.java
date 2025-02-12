package com.vladislav;

import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

@EnableAutoConfiguration
@ComponentScan
public class ApplicationConfiguration {
    @Bean
    @Primary
    public Stage mainStage() {
        return new Stage();
    }
    @Bean
    public SimpleDateFormat primaryDateFormat() {
        return new SimpleDateFormat("HH:mm dd.MM.yyyy");
    }
}
