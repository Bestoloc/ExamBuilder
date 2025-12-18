package ticketbuilder.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    public Button btnCreate;
    public Button btnAnalysis;
    @FXML private AnchorPane root;

    @FXML
    private void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    private void onCreate() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/create.fxml")
            );
            Scene scene = new Scene(loader.load());
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
    private void onAddQuestion() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/add_question.fxml")
            );
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setTitle("Добавить вопрос");
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
