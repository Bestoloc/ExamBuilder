package com.examsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Пробуем загрузить FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();

            // Создаем сцену
            Scene scene = new Scene(root);
            // Устанавливаем заголовок
            primaryStage.setTitle("ExamPro System - Вход");
            primaryStage.setScene(scene);
            // Центрирование окна на экране
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            // Показать окно в размере, заданном в FXML
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            // Создаем простое окно с сообщением об ошибке
            try {
                Label errorLabel = new Label("Ошибка загрузки интерфейса. Проверьте структуру файлов.\n" +
                        "Должно быть: src/main/resources/fxml/login.fxml");
                errorLabel.setStyle("-fx-text-fill: red; -fx-padding: 20; -fx-font-size: 14;");
                StackPane root = new StackPane(errorLabel);
                Scene scene = new Scene(root, 600, 400);
                primaryStage.setTitle("Ошибка");
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Проверяем наличие ресурсов
        System.out.println("Проверка ресурсов:");
        System.out.println("FXML: " + Main.class.getResource("/fxml/login.fxml"));
        System.out.println("CSS: " + Main.class.getResource("/style.css"));

        launch(args);
    }
}