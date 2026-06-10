package com.disasterrelief;

import com.disasterrelief.service.UserService;
import com.disasterrelief.service.AdminService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginView {

    private final VBox root = new VBox(15);
    private final UserService userService = new UserService();
    private final AdminService adminService = new AdminService();
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
        build();
    }

    private void build() {
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Título
        Text titulo = new Text("HelpMore");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 36px; -fx-font-weight: bold;");

        Text subtitulo = new Text("Redes de Apoyo en Desastres");
        subtitulo.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px;");

        Text upc = new Text("Universidad Popular del Cesar");
        upc.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 11px;");

        // Campos
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");
        passField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 12px;");

        // Botones
        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnRegistro = new Button("Registrarse");
        btnRegistro.setMaxWidth(Double.MAX_VALUE);
        btnRegistro.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnAnonimo = new Button("Donar como Anónimo");
        btnAnonimo.setMaxWidth(Double.MAX_VALUE);
        btnAnonimo.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnAdmin = new Button("Acceso Administrador");
        btnAdmin.setMaxWidth(Double.MAX_VALUE);
        btnAdmin.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");

        // Acciones
        btnLogin.setOnAction(e -> {
            try {
                var usuario = userService.login(emailField.getText(), passField.getText());
                new UserDashboard(stage, usuario, userService).show();
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        btnRegistro.setOnAction(e -> new RegisterView(stage, userService).show());

        btnAnonimo.setOnAction(e -> new AnonDonationView(stage, userService).show());

        btnAdmin.setOnAction(e -> new AdminView(stage, adminService, userService).show());

        root.getChildren().addAll(
                titulo, subtitulo, upc,
                new Separator(),
                emailField, passField,
                errorLabel,
                btnLogin, btnRegistro,
                new Separator(),
                btnAnonimo, btnAdmin
        );
    }

    public VBox getView() { return root; }
}