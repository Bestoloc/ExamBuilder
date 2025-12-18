package ticketbuilder.controllers;

import javafx.animation.FadeTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ticketbuilder.dao.UserDAO;
import ticketbuilder.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML public javafx.scene.control.TextField textLogin;
    @FXML public javafx.scene.control.Label labelError;
    public Button btnJoin;
    @FXML private PasswordField txtPassword;
    @FXML private AnchorPane root;
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

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
}
