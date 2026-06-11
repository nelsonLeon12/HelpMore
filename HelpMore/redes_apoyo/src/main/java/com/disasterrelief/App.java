package com.disasterrelief;

import com.disasterrelief.service.AdminService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            AdminService adminService = new AdminService();
            adminService.createAdmin("Admin Principal", "Valledupar", "3001234567");
        } catch (Exception e) {
            // Si ya existe no importa
        }

        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getView(), 420, 600);
        stage.setTitle("HelpMore - Redes de Apoyo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}