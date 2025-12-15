package ticketbuilder.controllers;

import ticketbuilder.dao.*;
import ticketbuilder.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ticketbuilder.dao.StudentDAO;
import ticketbuilder.dao.TicketHistoryDAO;
import ticketbuilder.model.Question;
import ticketbuilder.model.Student;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreateController {

    public Button btnBack;
    @FXML
    private ComboBox<Student> cmbStudents;
    @FXML
    private TextArea txtTicket;

    private final TicketHistoryDAO historyDAO = new TicketHistoryDAO();

    private int currentTicketId;

    private final StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {
        loadStudents();
    }

    private void loadStudents() {
        cmbStudents.getItems().clear();
        cmbStudents.getItems().addAll(studentDAO.findAll());
    }

    @FXML
    private void generateTicket() {

        Student student = cmbStudents.getValue();
        if (student == null) {
            showAlert("Выберите ученика");
            return;
        }

        List<Question> questions =
                historyDAO.generateTicket(student.getId(), 3);

        currentTicketId = historyDAO.getLastTicketId();

        showTicket(questions);
        enterScores(questions);
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

    private void enterScores(List<Question> questions) {

        for (Question q : questions) {

            TextInputDialog scoreDialog = new TextInputDialog();
            scoreDialog.setTitle("Оценка");
            scoreDialog.setHeaderText("Введите оценку за вопрос");
            scoreDialog.setContentText(q.getText());

            Optional<String> scoreResult = scoreDialog.showAndWait();
            if (scoreResult.isEmpty()) continue;

            int score = Integer.parseInt(scoreResult.get());

            TextInputDialog commentDialog = new TextInputDialog();
            commentDialog.setTitle("Комментарий");
            commentDialog.setHeaderText("Комментарий к вопросу");
            commentDialog.setContentText("Комментарий:");

            String comment = commentDialog.showAndWait().orElse(null);

            historyDAO.updateScoreAndComment(
                    currentTicketId,
                    q.getId(),
                    score,
                    comment
            );
        }
    }


    private void showTicket(List<Question> questions) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Question q : questions) {
            sb.append(i++).append(". ").append(q.getText()).append("\n\n");
        }
        txtTicket.setText(sb.toString());
    }

    private void showAlert(String text) {
        new Alert(Alert.AlertType.WARNING, text).show();
    }
}

