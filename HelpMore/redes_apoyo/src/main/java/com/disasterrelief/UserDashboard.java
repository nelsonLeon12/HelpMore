package com.disasterrelief;

import com.disasterrelief.entities.Database;
import com.disasterrelief.entities.PermanentUser;
import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

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
        Database db = Database.getInstance();

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Bienvenido, " + usuario.getName());
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 24px; -fx-font-weight: bold;");

        Text puntos = new Text("Puntos donación: " + usuario.getDonationPoints()
                + "  |  Puntos ayuda: " + usuario.getReliefPoints());
        puntos.setStyle("-fx-fill: #aaaaaa; -fx-font-size: 13px;");

        // Alertas activas
        Text tituloAlertas = new Text("Alertas Activas:");
        tituloAlertas.setStyle("-fx-fill: #e94560; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox alertasBox = new VBox(5);
        alertasBox.setPadding(new Insets(10));
        alertasBox.setStyle("-fx-background-color: #16213e;");

        List<String> alertas = db.getAlertasActivas();
        if (alertas.isEmpty()) {
            Label noAlertas = new Label("Sin alertas activas en tu zona.");
            noAlertas.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");
            alertasBox.getChildren().add(noAlertas);
        } else {
            for (String alerta : alertas) {
                Label lbl = new Label("🚨 " + alerta);
                lbl.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 12px;");
                lbl.setWrapText(true);
                alertasBox.getChildren().add(lbl);
            }
        }

        Label msgLabel = new Label("");
        msgLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");

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
                msgLabel.setStyle("-fx-text-fill: #00ff88;");
                msgLabel.setText("Donacion exitosa! Gracias " + usuario.getName());
                montoField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

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
                msgLabel.setText("Registrado en zona " + zonaField.getText() + "!");
                zonaField.clear();
            } catch (Exception ex) {
                msgLabel.setStyle("-fx-text-fill: #e94560;");
                msgLabel.setText(ex.getMessage());
            }
        });

        Button btnRanking = new Button("Ver Ranking");
        btnRanking.setMaxWidth(Double.MAX_VALUE);
        btnRanking.setStyle("-fx-background-color: #16213e; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-padding: 10; -fx-cursor: hand;");
        btnRanking.setOnAction(e -> new RankingView(stage, userService).show());

        Button btnSalir = new Button("Cerrar Sesion");
        btnSalir.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");
        btnSalir.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        root.getChildren().addAll(
                titulo, puntos,
                new Separator(),
                tituloAlertas, alertasBox,
                new Separator(),
                montoField, btnDonar,
                new Separator(),
                zonaField, btnZona,
                new Separator(),
                btnRanking, msgLabel, btnSalir
        );

        stage.getScene().setRoot(root);
    }
}