package org.example.bearfitness;

import org.example.bearfitness.data.DBService;
import org.example.bearfitness.ui.LoginUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class BearFitnessApplication {
    public static void main(String[] args) {
        //SpringApplication.run(BearFitnessApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(BearFitnessApplication.class, args);
        DBService dbService = context.getBean(DBService.class);


        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI(dbService);
            loginUI.setVisible(true);
        });

    }

}
