package com.disasterrelief;

import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnonDonationView {

    private final Stage stage;
    private final UserService userService;

    public AnonDonationView(Stage stage, UserService userService) {
        this.stage = stage;
        this.userService = userService;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Donación Anónima");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Text sub = new Text("Tu ayuda es valiosa aunque no te identifiques");
        sub.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 13px;");

        TextField montoField = new TextField();
        montoField.setPromptText("Monto a donar ($)");
        montoField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 13px;");

        Button btnDonar = new Button("Donar Ahora");
        btnDonar.setMaxWidth(Double.MAX_VALUE);
        btnDonar.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");

        btnDonar.setOnAction(e -> {
            try {
                double monto = Double.parseDouble(montoField.getText().trim());
                userService.donateAnonymously(monto);
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("¡Donación exitosa! Gracias por tu apoyo.");
                montoField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        root.getChildren().addAll(titulo, sub, montoField, msgLabel, btnDonar, btnVolver);
        stage.getScene().setRoot(root);
    }
}