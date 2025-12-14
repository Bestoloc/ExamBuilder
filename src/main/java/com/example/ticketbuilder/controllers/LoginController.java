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
        close();
    }

    private void openMain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = (Stage) btnJoin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene(); // ← ВАЖНО
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        ((Stage) textLogin.getScene().getWindow()).close();
    }
}
