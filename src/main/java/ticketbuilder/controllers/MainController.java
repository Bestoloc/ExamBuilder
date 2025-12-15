package ticketbuilder.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public Button btnCreate;
    public Button btnAnalysis;

    @FXML
    private void onCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/create.fxml")
            );
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/style.css").toExternalForm()
            );
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setTitle("Авторизация");
            stage.setScene(scene);      // ВАЖНО
            stage.sizeToScene();        // берёт размеры из FXML
            stage.setResizable(false);
            stage.show();

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void onAnalysis() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/analysis.fxml")
            );
            Scene scene = new Scene(loader.load());

            scene.getStylesheets().add(
                    getClass().getResource("/style.css").toExternalForm()
            );
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setTitle("Анализ");
            stage.setScene(scene);      //  ВАЖНО
            stage.sizeToScene();        //  берёт размеры из FXML
            stage.setResizable(false);
            stage.show();

        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
