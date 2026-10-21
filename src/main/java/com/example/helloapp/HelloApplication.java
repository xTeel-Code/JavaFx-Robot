package com.example.helloapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setController(new HelloController());

        // z menu sa otvara okno s robotom
        MenuItem robot = new MenuItem("Robot");
        robot.setOnAction(e -> RobotApplication.otvor(new Stage()));
        MenuBar menu = new MenuBar(new Menu("Aplikácie", null, robot));

        BorderPane root = new BorderPane(fxmlLoader.load());
        root.setTop(menu);

        Scene scene = new Scene(root);
        stage.setTitle("Kalk");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
