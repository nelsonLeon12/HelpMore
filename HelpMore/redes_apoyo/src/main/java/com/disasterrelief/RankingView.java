package com.disasterrelief;

import com.disasterrelief.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RankingView {

    private final Stage stage;
    private final UserService userService;

    public RankingView(Stage stage, UserService userService) {
        this.stage = stage;
        this.userService = userService;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Text titulo = new Text("Rankings HelpMore");
        titulo.setStyle("-fx-fill: #e94560; -fx-font-size: 28px; -fx-font-weight: bold;");

        Text t1 = new Text("🏆 Ranking Donaciones");
        t1.setStyle("-fx-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox donacionBox = new VBox(5);
        for (java.util.Map.Entry<String, Integer> entry : userService.getDonationRanking()) {
            Label l = new Label("  " + entry.getKey() + " — " + entry.getValue() + " pts");
            l.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 13px;");
            donacionBox.getChildren().add(l);
        }

        Text t2 = new Text("🤝 Ranking Ayuda Física");
        t2.setStyle("-fx-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox ayudaBox = new VBox(5);
        for (java.util.Map.Entry<String, Integer> entry : userService.getReliefRanking()) {
            Label l = new Label("  " + entry.getKey() + " — " + entry.getValue() + " pts");
            l.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 13px;");
            ayudaBox.getChildren().add(l);
        }

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaaaaa; -fx-font-size: 13px; -fx-cursor: hand;");
        btnVolver.setOnAction(e -> stage.getScene().setRoot(new LoginView(stage).getView()));

        root.getChildren().addAll(titulo, new Separator(), t1, donacionBox, new Separator(), t2, ayudaBox, new Separator(), btnVolver);
        stage.getScene().setRoot(root);
    }
}