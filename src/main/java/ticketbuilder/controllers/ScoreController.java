package ticketbuilder.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ticketbuilder.dao.TicketHistoryDAO;
import ticketbuilder.model.ScoreRow;
import ticketbuilder.model.TicketHistory;
import ticketbuilder.util.HibernateUtil;

import java.io.IOException;
import java.util.List;

public class ScoreController {

    public Button btnBack;
    @FXML private TableView<ScoreRow> table;
    @FXML private TableColumn<ScoreRow, String> colQuestion;
    @FXML private TableColumn<ScoreRow, Integer> colScore;
    @FXML private TableColumn<ScoreRow, String> colComment;

    private List<TicketHistory> historyList;

    public void setHistory(List<TicketHistory> historyList) {
        this.historyList = historyList;
        historyList.forEach(h -> table.getItems().add(new ScoreRow(h)));
    }

    @FXML
    public void initialize() {

        table.setEditable(true);

        colQuestion.setCellValueFactory(
                data -> data.getValue().questionTextProperty()
        );

        colScore.setCellValueFactory(
                data -> data.getValue().scoreProperty().asObject()
        );

        colComment.setCellValueFactory(
                data -> data.getValue().commentProperty()
        );

        // 🔥 РЕДАКТИРУЕМЫЕ ЯЧЕЙКИ
        colScore.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new IntegerStringConverter()
                )
        );

        colComment.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );
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

    @FXML
    private void onSave() {

        for (ScoreRow row : table.getItems()) {

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
}
