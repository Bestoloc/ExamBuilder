package com.example.ticketbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/fxml/login.fxml")
        );
        stage.setTitle("Авторизация");
        stage.setScene(new Scene(root, 350, 250));
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
