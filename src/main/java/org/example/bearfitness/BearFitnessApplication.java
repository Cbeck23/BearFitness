package org.example.bearfitness;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.example.bearfitness.data.DBService;
import org.example.bearfitness.ui.LoginUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.bearfitness.ui.ScreenManager;
import javax.swing.*;

@SpringBootApplication
public class BearFitnessApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BearFitnessApplication.class, args);
        DBService dbService = context.getBean(DBService.class);

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new ScreenManager(dbService); // Starts at login screen
        });
    }
}
