package ticketbuilder.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
import ticketbuilder.util.HibernateUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreateController {

    public Button btnBack;
    public Button btnGenerate;

    @FXML private TableView<ScoreRow> tableTicket;
    @FXML private TableColumn<ScoreRow, String> colQuestion;
    @FXML private TableColumn<ScoreRow, Integer> colScore;
    @FXML private TableColumn<ScoreRow, String> colComment;
    @FXML private TableColumn<ScoreRow, Number> colDifficulty;

    @FXML private ComboBox<Topic> cmbTopics;
    @FXML private ComboBox<Student> cmbStudents;
    @FXML private VBox ticketBox;
    @FXML private TextArea txtTicket;
    @FXML private AnchorPane root;


    private final TopicDAO topicDAO = new TopicDAO();

    private final TicketHistoryDAO historyDAO = new TicketHistoryDAO();

    private int currentTicketId;

    private final StudentDAO studentDAO = new StudentDAO();

    private List<TicketHistory> currentHistory;

    public void setHistory(List<TicketHistory> currentHistory) {
        this.currentHistory = currentHistory;
        currentHistory.forEach(h -> tableTicket.getItems().add(new ScoreRow(h)));
    }

    @FXML
    public void initialize() {
        loadStudents();
        cmbTopics.getItems().addAll(TopicDAO.findAll());
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        tableTicket.setEditable(true);

        colQuestion.setCellValueFactory(
                d -> d.getValue().questionTextProperty());

        colScore.setCellValueFactory(
                d -> d.getValue().scoreProperty().asObject());

        colComment.setCellValueFactory(
                d -> d.getValue().commentProperty());

        colDifficulty.setCellValueFactory(
                d -> d.getValue().difficultyProperty());

        colScore.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new IntegerStringConverter()));

        colComment.setCellFactory(
                TextFieldTableCell.forTableColumn());
        colDifficulty.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(
                            switch (item.intValue()) {
                                case 1 -> "Лёгкий";
                                case 2 -> "Средний";
                                case 3 -> "Сложный";
                                default -> "Неизвестно";
                            }
                    );
                }
            }
        });
    }

    @FXML
    private void onSaveScores() {

        for (ScoreRow row : tableTicket.getItems()) {

            TicketHistory h = row.getHistory();
            h.setScore(row.scoreProperty().get());
            h.setComment(row.commentProperty().get());

            TicketHistoryDAO.update(h);
        }

        showAlert("Оценки сохранены");
    }

    private void showAlert(String text) {
        new Alert(Alert.AlertType.INFORMATION, text).show();
    }

    private void loadStudents() {
        cmbStudents.getItems().clear();
        cmbStudents.getItems().addAll(StudentDAO.findAll());
    }

    @FXML
    private TicketResult generateTicket(Student student, Topic topic) throws Exception {

        if (student == null) {
            showAlert("Выберите ученика");
            return null;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        TicketResult result = TicketHistoryDAO.generateTicket(student, topic);

        List<Question> questions = result.getQuestions();

        int ticketId = (int) (System.currentTimeMillis() / 1000);

        for (Question q : questions) {
            TicketHistory th = new TicketHistory();
            th.setTicketId(ticketId);
            th.setStudent(student);
            th.setQuestion(q);
            session.persist(th);
        }

        tx.commit();
        session.close();
        return new TicketResult(ticketId, questions);
    }

    @FXML
    private void onGenerate() {

        Student student = cmbStudents.getValue();
        Topic topic = cmbTopics.getValue();

        if (student == null || topic == null) {
            showAlert("Выберите ученика и тему");
            return;
        }

        TicketResult result =
                TicketHistoryDAO.generateTicket(student, topic);

        currentHistory =
                TicketHistoryDAO.findByTicketId(result.getTicketId());

        tableTicket.getItems().clear();

        currentHistory.forEach(h ->
                tableTicket.getItems().add(new ScoreRow(h)));
    }


    /*private void openScoreWindow(int ticketId) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/score.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("Оценивание билета");
            stage.setScene(scene);
            stage.setResizable(false);

            // 🔥 передаём ТОЛЬКО вопросы этого билета
            ScoreController controller = loader.getController();
            controller.setHistory(
                    TicketHistoryDAO.findByTicketId(ticketId)
            );

            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); // ❗ ОБЯЗАТЕЛЬНО
            showAlert("Ошибка открытия окна оценивания");
        }
    }*/


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

    /*private void enterScores(List<Question> questions) {

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
    }*/


    private void showTicket(List<Question> questions) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Question q : questions) {
            sb.append(i++).append(". ").append(q.getText()).append("\n\n");
        }
        txtTicket.setText(sb.toString());
    }
}

