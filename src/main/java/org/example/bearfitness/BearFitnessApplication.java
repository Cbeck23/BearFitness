package org.example.bearfitness;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.UI.LoginUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.bearfitness.UI.ScreenManager;
import javax.swing.*;

@SpringBootApplication
public class BearFitnessApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BearFitnessApplication.class, args);
        DBService dbService = context.getBean(DBService.class);

        SwingUtilities.invokeLater(() -> {
            new ScreenManager(dbService); // Starts at login screen
        });
    }
}
