package com.example.ticketbuilder.controllers;

import com.example.ticketbuilder.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    public Button btnCreate;
    public Button btnAnalysis;

    @FXML
    private void onCreate() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/create.fxml"));
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene(); // ← ВАЖНО
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onAnalysis() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/analysis.fxml"));
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene(); // ← ВАЖНО
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
