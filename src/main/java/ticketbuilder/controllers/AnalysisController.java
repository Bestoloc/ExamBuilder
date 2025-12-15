package ticketbuilder.controllers;

import ticketbuilder.dao.StudentDAO;
import ticketbuilder.dao.TicketHistoryDAO;
import ticketbuilder.model.AnalysisRow;
import ticketbuilder.model.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnalysisController {

    public Button btnBack;
    @FXML private TableView<AnalysisRow> table;

    @FXML private TableColumn<AnalysisRow, Integer> colId;
    @FXML private TableColumn<AnalysisRow, String> colText;
    @FXML private TableColumn<AnalysisRow, Long> colUsed;
    @FXML private TableColumn<AnalysisRow, Double> colAvg;
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

    @FXML private ComboBox<Student> cmbStudents;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("questionId"));
        colText.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        colUsed.setCellValueFactory(new PropertyValueFactory<>("usedCount"));
        colAvg.setCellValueFactory(new PropertyValueFactory<>("averageScore"));
        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        cmbStudents.getItems().addAll(StudentDAO.findAll());

        cmbStudents.setOnAction(e -> loadStudentAnalysis());
    }

    private void loadStudentAnalysis() {

        Student s = cmbStudents.getValue();
        if (s == null) return;

        List<Object[]> rawData =
                TicketHistoryDAO.getAnalysisByStudent(s.getId());

        List<AnalysisRow> rows = new ArrayList<>();

        for (Object[] r : rawData) {

            Integer qId = (Integer) r[0];
            String text = (String) r[1];

            Number used = (Number) r[2];
            Number avg = (Number) r[3];

            String comment = (String) r[4];

            rows.add(new AnalysisRow(
                    qId,
                    text,
                    used.longValue(),
                    avg != null ? avg.doubleValue() : 0.0,
                    comment
            ));
        }
        table.getItems().setAll(rows);
    }

    @FXML
    private void onGlobalAnalysis() {

        List<Object[]> rawData = TicketHistoryDAO.getGlobalAnalysis();
        List<AnalysisRow> rows = new ArrayList<>();

        for (Object[] r : rawData) {

            Integer qId = (Integer) r[0];
            String text = (String) r[1];

            Number used = (Number) r[2];
            Number avg = (Number) r[3];

            rows.add(new AnalysisRow(
                    qId,
                    text,
                    used.longValue(),
                    avg != null ? avg.doubleValue() : 0.0,
                    null   // комментариев нет в общей статистике
            ));
        }

        table.getItems().setAll(rows);
    }

}
