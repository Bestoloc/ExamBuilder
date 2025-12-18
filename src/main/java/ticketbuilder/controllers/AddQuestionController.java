package ticketbuilder.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ticketbuilder.dao.QuestionDAO;
import ticketbuilder.dao.TopicDAO;
import ticketbuilder.model.Question;
import ticketbuilder.model.Topic;

import java.io.IOException;

public class AddQuestionController {

    public Button btnBack;
    @FXML private TextArea txtQuestion;
    @FXML private AnchorPane root;
    @FXML private TextField txtAnswer;
    @FXML private TextField txtDifficulty;
    @FXML private ComboBox<Topic> cmbTopics;

    private final QuestionDAO questionDAO = new QuestionDAO();

    @FXML
    public void initialize() {
        cmbTopics.getItems().addAll(TopicDAO.findAll());
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    private void onSave() {

        if (cmbTopics.getValue() == null) {
            showAlert("Выберите тему");
            return;
        }

        Question q = new Question();
        q.setText(txtQuestion.getText());
        q.setAnswer(txtAnswer.getText());
        q.setDifficulty(Integer.parseInt(txtDifficulty.getText()));
        q.setTopic(cmbTopics.getValue());

        // обязательные поля
        q.setSubjectId(1);
        q.setTypeId(1);

        questionDAO.save(q);
        showAlert("Вопрос успешно добавлен!");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void onBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Главное окно");
            stage.sizeToScene();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}

