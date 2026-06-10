package com.disasterrelief;

import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserDashboard {

    private final Stage stage;
    private final PermanentUser usuario;
    private final UserService userService;

    public UserDashboard(Stage stage, PermanentUser usuario, UserService userService) {
        this.stage = stage;
        this.usuario = usuario;
        this.userService = userService;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Bienvenido, " + usuario.getName());
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 24px; -fx-font-weight: bold;");

        Text puntos = new Text("Puntos donación: " + usuario.getDonationPoints()
                + "  |  Puntos ayuda: " + usuario.getReliefPoints());
        puntos.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 13px;");

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");

        // Donar
        TextField montoField = new TextField();
        montoField.setPromptText("Monto a donar ($)");
        montoField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        Button btnDonar = new Button("Donar");
        btnDonar.setMaxWidth(Double.MAX_VALUE);
        btnDonar.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        btnDonar.setOnAction(e -> {
            try {
                double monto = Double.parseDouble(montoField.getText().trim());
                userService.donate(usuario.getId(), monto);
                msgLabel.setText("¡Donación exitosa! Gracias " + usuario.getName());
                montoField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        // Acudir a zona
        TextField zonaField = new TextField();
        zonaField.setPromptText("Zona de emergencia");
        zonaField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        Button btnZona = new Button("Acudir a Zona");
        btnZona.setMaxWidth(Double.MAX_VALUE);
        btnZona.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        btnZona.setOnAction(e -> {
            try {
                userService.goToZone(usuario.getId(), zonaField.getText());
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("¡Registrado en zona " + zonaField.getText() + "!");
                zonaField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        // Ranking
        Button btnRanking = new Button("Ver Ranking");
        btnRanking.setMaxWidth(Double.MAX_VALUE);
        btnRanking.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");

        btnRanking.setOnAction(e -> new RankingView(stage, userService).show());

        // Cerrar sesión
        Button btnSalir = new Button("Cerrar Sesión");
        btnSalir.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        root.getChildren().addAll(
                titulo, puntos, new Separator(),
                montoField, btnDonar,
                new Separator(),
                zonaField, btnZona,
                new Separator(),
                btnRanking, msgLabel, btnSalir
        );

        stage.getScene().setRoot(root);
    }
}