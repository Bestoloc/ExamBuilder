package com.examsystem.controllers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CheckBoxTableCellFactory<S, T> implements
        Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<S, T>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setOnAction(event -> {
                    if (getTableRow() != null) {
                        S rowData = getTableRow().getItem();
                        if (rowData instanceof CreateTicketController.StudentWrapper) {
                            ((CreateTicketController.StudentWrapper) rowData)
                                    .setSelected(checkBox.isSelected());
                        }
                    }
                });
            }

            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if (item instanceof Boolean) {
                        checkBox.setSelected((Boolean) item);
                    } else if (item instanceof CreateTicketController.StudentWrapper) {
                        checkBox.setSelected(
                                ((CreateTicketController.StudentWrapper) item).isSelected()
                        );
                    }
                    setGraphic(checkBox);
                }
            }
        };
    }
}