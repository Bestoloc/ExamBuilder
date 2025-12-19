package com.examsystem.controllers;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class StatusCellFactory implements
        Callback<TableColumn<AnalysisController.AnalysisWrapper, String>,
                TableCell<AnalysisController.AnalysisWrapper, String>> {

    @Override
    public TableCell<AnalysisController.AnalysisWrapper, String> call(TableColumn<AnalysisController.AnalysisWrapper, String> param) {
        return new TableCell<AnalysisController.AnalysisWrapper, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    switch (item) {
                        case "Отлично":
                            setStyle("-fx-background-color: #10b981; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-background-radius: 4; " +
                                    "-fx-padding: 2 8;");
                            break;
                        case "Нормально":
                            setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-background-radius: 4; " +
                                    "-fx-padding: 2 8;");
                            break;
                        case "Требует внимания":
                            setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-background-radius: 4; " +
                                    "-fx-padding: 2 8;");
                            break;
                        case "Переписать!":
                            setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; " +
                                    "-fx-font-weight: bold; -fx-background-radius: 4; " +
                                    "-fx-padding: 2 8;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        };
    }
}