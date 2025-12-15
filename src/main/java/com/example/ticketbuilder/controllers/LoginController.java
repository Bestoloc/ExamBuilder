package com.example.ticketbuilder.controllers;

import com.example.ticketbuilder.dao.UserDAO;
import com.example.ticketbuilder.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jdk.jfr.Label;

import java.awt.*;
import java.awt.TextField;
import java.io.IOException;

public class LoginController {

    @FXML public javafx.scene.control.TextField textLogin;
    @FXML public javafx.scene.control.Label labelError;
    public Button btnJoin;
    @FXML private PasswordField txtPassword;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void onLogin() {
        User user = userDAO.login(
                textLogin.getText(),
                txtPassword.getText()
        );

        if (user == null || !"TEACHER".equals(user.getRole())) {
            labelError.setText("Неверный логин или пароль");
            return;
        }

        openMain();
    }

    private void openMain() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/main.fxml")
            );
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/style.css").toExternalForm()
            );
            Stage stage = (Stage) btnJoin.getScene().getWindow();
            stage.setTitle("Главное окно");
            stage.setScene(scene);      // ✅ ВАЖНО
            stage.sizeToScene();        // ✅ берёт размеры из FXML
            stage.setResizable(false);
            stage.show();

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void close() {
        ((Stage) textLogin.getScene().getWindow()).close();
    }
}
