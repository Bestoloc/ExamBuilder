package ticketbuilder.controllers;

import javafx.animation.FadeTransition;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.hibernate.Session;
import ticketbuilder.dao.StudentDAO;
import ticketbuilder.dao.TicketHistoryDAO;
import ticketbuilder.model.AnalysisRow;
import ticketbuilder.model.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ticketbuilder.util.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ticketbuilder.dao.TicketHistoryDAO.getAnalysisByStudent;

public class AnalysisController {

    public Button btnBack;
    @FXML private TableView<AnalysisRow> tableAnalysis;
    @FXML private AnchorPane root;
    @FXML private ComboBox<Student> cmbStudents;

    @FXML private TableColumn<AnalysisRow, Number> colId;
    @FXML private TableColumn<AnalysisRow, String> colText;
    @FXML private TableColumn<AnalysisRow, String> colTopic;
    @FXML private TableColumn<AnalysisRow, Number> colDifficulty;
    @FXML private TableColumn<AnalysisRow, Number> colUsage;
    @FXML private TableColumn<AnalysisRow, Number> colAvg;
    @FXML private TableColumn<AnalysisRow, String> colComment;

    private final TicketHistoryDAO dao = new TicketHistoryDAO();

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

    @FXML
    public void initialize() {
        cmbStudents.getItems().addAll(StudentDAO.findAll());

        colId.setCellValueFactory(d -> d.getValue().questionIdProperty());
        colText.setCellValueFactory(d -> d.getValue().questionTextProperty());
        colTopic.setCellValueFactory(d -> d.getValue().topicNameProperty());
        colDifficulty.setCellValueFactory(d -> d.getValue().difficultyProperty());
        colUsage.setCellValueFactory(d -> d.getValue().usageCountProperty());
        colAvg.setCellValueFactory(d -> d.getValue().averageScoreProperty());
        colComment.setCellValueFactory(d -> d.getValue().commentProperty());

        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

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
    private void onStudentSelected() {
        tableAnalysis.getItems().clear(); // 🔥 ОБЯЗАТЕЛЬНО
        Student student = cmbStudents.getValue();
        if (student == null) {
            return;
        }

        List<AnalysisRow> rows =
                TicketHistoryDAO.getAnalysisByStudent(student);

        tableAnalysis.getItems().addAll(rows);
    }



    @FXML
    private void onGlobalAnalysis() {

        List<Object[]> rawData = TicketHistoryDAO.getGlobalAnalysis();
        List<AnalysisRow> rows = new ArrayList<>();

        for (Object[] r : rawData) {

            Integer qId = (Integer) r[0];
            String text = (String) r[1];
            String topic = (String) r[2];
            Integer difficulty = (Integer) r[3];
            Long used = (Long) r[4];
            Double avg = (Double) r[5];

            rows.add(new AnalysisRow(
                    qId,
                    text,
                    topic,
                    difficulty,
                    used,
                    avg != null ? avg : 0.0,
                    null   // комментариев нет в общей статистике
            ));
        }

        tableAnalysis.getItems().setAll(rows);
    }

}
