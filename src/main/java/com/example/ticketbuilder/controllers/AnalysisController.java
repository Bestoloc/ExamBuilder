package com.example.ticketbuilder.controllers;

import com.example.ticketbuilder.dao.TicketHistoryDAO;
import com.example.ticketbuilder.model.AnalysisRow;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AnalysisController {

    public Button btnBack;
    @FXML private TableView<AnalysisRow> table;
    @FXML private TableColumn<AnalysisRow, String> colText;
    @FXML private TableColumn<AnalysisRow, Long> colUsed;
    @FXML private TableColumn<AnalysisRow, Double> colAvg;

    private final TicketHistoryDAO dao = new TicketHistoryDAO();

    @FXML
    private void onBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene(); // ← ВАЖНО
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }

    }

    @FXML
    private void initialize() {
        colText.setCellValueFactory(new PropertyValueFactory<>("text"));
        colUsed.setCellValueFactory(new PropertyValueFactory<>("usedCount"));
        colAvg.setCellValueFactory(new PropertyValueFactory<>("avgScore"));

        table.setItems(
                FXCollections.observableArrayList(
                        dao.getAnalysis().stream()
                                .map(r -> new AnalysisRow(
                                        (String) r[0],
                                        (Long) r[1],
                                        (Double) r[2]
                                ))
                                .toList()
                )
        );
    }
}
