package com.disasterrelief;
import javafx.scene.layout.HBox;
import com.disasterrelief.entities.Database;
import com.disasterrelief.service.AdminService;
import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

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
        VBox authRoot = new VBox(15);
        authRoot.setAlignment(Pos.CENTER);
        authRoot.setPadding(new Insets(40));
        authRoot.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Acceso Administrador");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Text sub = new Text("Ingresa la contrasena de administrador");
        sub.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 13px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contrasena");
        passField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10; -fx-font-size: 14px;");

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e94560; -fx-font-size: 12px;");

        Button btnEntrar = new Button("Entrar");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");

        btnEntrar.setOnAction(e -> {
            if (passField.getText().equals("2025")) {
                mostrarPanel();
            } else {
                errorLabel.setText("Contrasena incorrecta.");
            }
        });

        btnVolver.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        authRoot.getChildren().addAll(titulo, sub, passField, errorLabel, btnEntrar, btnVolver);
        stage.getScene().setRoot(authRoot);
    }

    private void mostrarPanel() {
        Database db = Database.getInstance();

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Panel Administrador");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");

        TextField zonaField = new TextField();
        zonaField.setPromptText("Zona de emergencia");
        zonaField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        TextField msgField = new TextField();
        msgField.setPromptText("Mensaje de alerta");
        msgField.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; -fx-prompt-text-fill: #888; -fx-padding: 10;");

        Button btnAlerta = new Button("Activar Alerta de Emergencia");
        btnAlerta.setMaxWidth(Double.MAX_VALUE);
        btnAlerta.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");

        Text tituloAlertas = new Text("Alertas Activas:");
        tituloAlertas.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox alertasBox = new VBox(8);
        alertasBox.setPadding(new Insets(10));
        alertasBox.setStyle("-fx-background-color: #16213e;");

        Runnable[] refrescar = new Runnable[1];
        refrescar[0] = () -> {
            alertasBox.getChildren().clear();
            List<String> alertas = db.getAlertasActivas();
            if (alertas.isEmpty()) {
                Label noAlertas = new Label("No hay alertas activas.");
                noAlertas.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
                alertasBox.getChildren().add(noAlertas);
            } else {
                for (int i = 0; i < alertas.size(); i++) {
                    HBox fila = new HBox(10);
                    fila.setAlignment(Pos.CENTER_LEFT);
                    Label lblAlerta = new Label(alertas.get(i));
                    lblAlerta.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px;");
                    lblAlerta.setWrapText(true);
                    lblAlerta.setMaxWidth(250);
                    Button btnDesactivar = new Button("Desactivar");
                    btnDesactivar.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 11px; -fx-cursor: hand;");
                    final int index = i;
                    btnDesactivar.setOnAction(ev -> {
                        db.desactivarAlerta(index);
                        msgLabel.setStyle("-fx-text-fill: #00ff88;");
                        msgLabel.setText("Alerta desactivada.");
                        refrescar[0].run();
                    });
                    fila.getChildren().addAll(lblAlerta, btnDesactivar);
                    alertasBox.getChildren().add(fila);
                }
            }
        };

        btnAlerta.setOnAction(e -> {
            try {
                adminService.activateEmergencyAlert(1, zonaField.getText(), msgField.getText());
                int helpers = userService.getActiveUsers().size();
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("Alerta activada. Helpers notificados: " + helpers);
                zonaField.clear();
                msgField.clear();
                refrescar[0].run();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        Button btnFondos = new Button("Ver Fondos");
        btnFondos.setMaxWidth(Double.MAX_VALUE);
        btnFondos.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10; -fx-cursor: hand;");
        btnFondos.setOnAction(e -> {
            double saldo = adminService.getTotalFundBalance();
            msgLabel.setStyle("-fx-text-fill: #00ff88;");
            msgLabel.setText("Saldo total del fondo: $" + saldo);
        });

        Button btnUsuarios = new Button("Ver Usuarios Activos");
        btnUsuarios.setMaxWidth(Double.MAX_VALUE);
        btnUsuarios.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");
        btnUsuarios.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("Usuarios: ");
            userService.getActiveUsers().forEach(u -> sb.append(u.getName()).append(", "));
            msgLabel.setStyle("-fx-text-fill: #00ff88;");
            msgLabel.setText(sb.length() > 10 ? sb.toString() : "No hay usuarios activos.");
        });

        Button btnRanking = new Button("Ver Rankings");
        btnRanking.setMaxWidth(Double.MAX_VALUE);
        btnRanking.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");
        btnRanking.setOnAction(e -> new RankingView(stage, userService).show());

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        ScrollPane scroll = new ScrollPane(alertasBox);
        scroll.setFitToWidth(true);
        scroll.setMaxHeight(150);
        scroll.setStyle("-fx-background: #16213e;");

        refrescar[0].run();

        root.getChildren().addAll(
                titulo, new Separator(),
                zonaField, msgField, btnAlerta,
                new Separator(),
                tituloAlertas, scroll,
                new Separator(),
                btnFondos, btnUsuarios, btnRanking,
                msgLabel, new Separator(),
                btnVolver
        );

        stage.getScene().setRoot(root);
    }
}