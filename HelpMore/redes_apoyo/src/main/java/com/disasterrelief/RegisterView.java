package com.disasterrelief;

import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterView {

    private final Stage stage;
    private final UserService userService;

    public RegisterView(Stage stage, UserService userService) {
        this.stage = stage;
        this.userService = userService;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Registro");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre completo");
        nombreField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña (mín. 6 caracteres)");
        passField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 12px;");

        Button btnRegistrar = new Button("Crear cuenta");
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);
        btnRegistrar.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");

        btnRegistrar.setOnAction(e -> {
            try {
                userService.registerUser(nombreField.getText(), emailField.getText(), passField.getText());
                msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");
                msgLabel.setText("¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 12px;");
                msgLabel.setText(ex.getMessage());
            }
        });

        btnVolver.setOnAction(e -> {
            stage.getScene().setRoot(new LoginView(stage).getView());
        });

        root.getChildren().addAll(titulo, nombreField, emailField, passField, msgLabel, btnRegistrar, btnVolver);

        stage.getScene().setRoot(root);
    }
}