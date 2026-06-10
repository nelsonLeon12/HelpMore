package com.disasterrelief;

import com.disasterrelief.service.AdminService;
import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminView {

    private final Stage stage;
    private final AdminService adminService;
    private final UserService userService;

    public AdminView(Stage stage, AdminService adminService, UserService userService) {
        this.stage = stage;
        this.adminService = adminService;
        this.userService = userService;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Panel Administrador");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");

        // Activar alerta
        TextField zonaField = new TextField();
        zonaField.setPromptText("Zona de emergencia");
        zonaField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        TextField msgField = new TextField();
        msgField.setPromptText("Mensaje de alerta");
        msgField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        Button btnAlerta = new Button("🚨 Activar Alerta de Emergencia");
        btnAlerta.setMaxWidth(Double.MAX_VALUE);
        btnAlerta.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        btnAlerta.setOnAction(e -> {
            try {
                adminService.activateEmergencyAlert(1, zonaField.getText(), msgField.getText());
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("¡Alerta activada en zona " + zonaField.getText() + "!");
                zonaField.clear(); msgField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        // Ver fondos
        Button btnFondos = new Button("💰 Ver Fondos");
        btnFondos.setMaxWidth(Double.MAX_VALUE);
        btnFondos.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        btnFondos.setOnAction(e -> {
            try {
                double saldo = adminService.getTotalFundBalance();
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("Saldo total del fondo: $" + saldo);
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        // Ver usuarios
        Button btnUsuarios = new Button("👥 Ver Usuarios Activos");
        btnUsuarios.setMaxWidth(Double.MAX_VALUE);
        btnUsuarios.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");

        btnUsuarios.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("Usuarios: ");
            userService.getActiveUsers().forEach(u -> sb.append(u.getName()).append(", "));
            msgLabel.setStyle("-fx-text-fill: #00ff88;");
            msgLabel.setText(sb.toString());
        });

        // Ver ranking
        Button btnRanking = new Button("🏆 Ver Rankings");
        btnRanking.setMaxWidth(Double.MAX_VALUE);
        btnRanking.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");
        btnRanking.setOnAction(e -> new RankingView(stage, userService).show());

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        root.getChildren().addAll(
                titulo, new Separator(),
                zonaField, msgField, btnAlerta,
                new Separator(),
                btnFondos, btnUsuarios, btnRanking,
                msgLabel, new Separator(),
                btnVolver
        );

        stage.getScene().setRoot(root);
    }
}