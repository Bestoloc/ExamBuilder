package com.examsystem.controllers;

import com.examsystem.dao.TeacherDAO;
import com.examsystem.models.Teacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button clearButton;
    @FXML private Label errorLabel;

    private TeacherDAO teacherDAO = new TeacherDAO();

    @FXML
    public void initialize() {
        // Установить фокус на поле логина
        usernameField.requestFocus();

        // Добавить обработчик нажатия Enter
        usernameField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Пожалуйста, заполните все поля");
            return;
        }

        Teacher teacher = teacherDAO.authenticate(username, password);

        if (teacher != null) {
            try {
                // Закрыть текущее окно
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.close();

                // Открыть главное окно
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                Parent root = loader.load();

                // Передать данные преподавателя в MainController
                MainController mainController = loader.getController();
                mainController.setCurrentTeacher(teacher);

                Stage mainStage = new Stage();
                mainStage.setScene(new Scene(root));
                mainStage.setMaximized(true);
                mainStage.setTitle("Конструктор экзаменационных билетов");
                mainStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showError("Ошибка при открытии главного окна");
            }
        } else {
            showError("Неверный логин или пароль");
        }
    }

    @FXML
    private void handleClear() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
        usernameField.requestFocus();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}