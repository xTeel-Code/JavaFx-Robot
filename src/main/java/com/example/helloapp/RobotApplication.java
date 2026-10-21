package com.example.helloapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RobotApplication extends Application {
    @Override
    public void start(Stage stage) {
        Pane plocha = new Pane();
        plocha.setStyle("-fx-background-color: white;");
        // robot sa nesmie vykreslit mimo plochy
        plocha.setMinSize(0, 0);

        Robot robot = new Robot(plocha, 20, 20);
        plocha.getChildren().add(robot);

        Label info = new Label();
        info.setStyle("-fx-padding: 5;");

        BorderPane root = new BorderPane(plocha);
        root.setBottom(info);

        Scene scene = new Scene(root, 600, 400);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> robot.pohni(Robot.Smer.HORE);
                case S -> robot.pohni(Robot.Smer.DOLE);
                case A -> robot.pohni(Robot.Smer.VLAVO);
                case D -> robot.pohni(Robot.Smer.VPRAVO);
                default -> { }
            }
        });

        // stav rezimu zobrazujeme pod plochou
        info.textProperty().bind(robot.automatProperty().map(RobotApplication::popis));

        stage.setTitle("Robot");
        stage.setScene(scene);
        stage.show();
    }

    private static String popis(boolean automat) {
        return (automat ? "Režim: AUTOMATICKÝ" : "Režim: MANUÁLNY (WSAD)")
                + "  |  kliknutím na robota prepneš režim";
    }
}
