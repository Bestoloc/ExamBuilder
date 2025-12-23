package com.examsystem.controllers;

import com.examsystem.dao.BaseDAO;
import com.examsystem.dao.QuestionDAO;
import com.examsystem.dao.SubjectDAO;
import com.examsystem.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.awt.*;
import java.util.List;

public class AddQuestionController {

    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private ComboBox<Topic> topicComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ToggleGroup difficultyGroup;
    @FXML private TextArea questionTextArea;
    @FXML private TextArea answerTextArea;
    @FXML private Button saveButton;
    @FXML private Button btnAddSubject;
    @FXML private Button btnAddTopic;
    private BaseDAO baseDAO = new BaseDAO();

    @FXML private Label previewSubjectLabel;
    @FXML private Label previewTopicLabel;
    @FXML private Label previewTypeLabel;
    @FXML private Label previewDifficultyLabel;
    @FXML private TextArea previewQuestionText;

    // DAO объекты
    private SubjectDAO subjectDAO = new SubjectDAO();
    private QuestionDAO questionDAO = new QuestionDAO();

    // Данные
    private Teacher currentTeacher;

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        initializeData();
    }

    @FXML
    public void initialize() {
        // Инициализация происходит после setCurrentTeacher

        // Добавить слушатели для предпросмотра
        subjectComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        topicComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        typeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        difficultyGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        questionTextArea.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());
    }

    // Обновляем метод initializeData для деактивации кнопки добавления темы
    private void initializeData() {
        // Загрузить предметы
        refreshSubjects();

        // Установить обработчик выбора предмета
        subjectComboBox.setOnAction(e -> handleSubjectSelected());

        // Инициализировать кнопки
        btnAddTopic.setDisable(true); // Сначала деактивируем, пока не выбран предмет

        // Установить обработчик для текста вопроса
        questionTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 500) {
                questionTextArea.setText(oldVal);
            }
        });
    }


    @FXML
    private void handleSaveQuestion() {
        // Валидация данных
        if (!validateInput()) {
            return;
        }

        try {
            // Создать новый вопрос
            Question question = new Question();
            question.setSubject(subjectComboBox.getValue());
            question.setTopic(topicComboBox.getValue());
            question.setText(questionTextArea.getText().trim());
            question.setType(typeComboBox.getValue());

            // Получить выбранную сложность
            RadioButton selectedRadioButton = (RadioButton) difficultyGroup.getSelectedToggle();
            int difficulty = Integer.parseInt(selectedRadioButton.getUserData().toString());
            question.setDifficulty(difficulty);

            // Установить ответ (если есть)
            String answer = answerTextArea.getText().trim();
            if (!answer.isEmpty()) {
                question.setAnswer(answer);
            }

            question.setCreatedBy(currentTeacher);

            // Сохранить вопрос в базе данных
            questionDAO.saveQuestion(question);

            // Показать сообщение об успехе
            showSuccessAlert("Вопрос успешно сохранен!");

            // Очистить форму
            handleClear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось сохранить вопрос: " + e.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        Stage stage = (Stage) subjectComboBox.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (subjectComboBox.getValue() == null) {
            errors.append("• Выберите предмет\n");
        }

        if (topicComboBox.getValue() == null) {
            errors.append("• Выберите тему\n");
        }

        if (typeComboBox.getValue() == null) {
            errors.append("• Выберите тип вопроса\n");
        }

        if (difficultyGroup.getSelectedToggle() == null) {
            errors.append("• Выберите сложность\n");
        }

        String questionText = questionTextArea.getText().trim();
        if (questionText.isEmpty()) {
            errors.append("• Введите текст вопроса\n");
        } else if (questionText.length() < 10) {
            errors.append("• Текст вопроса слишком короткий (минимум 10 символов)\n");
        }

        if (errors.length() > 0) {
            showAlert("Ошибка валидации", errors.toString());
            return false;
        }

        return true;
    }

    private void updatePreview() {
        // Обновить информацию о предмете
        if (subjectComboBox.getValue() != null) {
            previewSubjectLabel.setText(subjectComboBox.getValue().getName());
        } else {
            previewSubjectLabel.setText("Не выбран");
        }

        // Обновить информацию о теме
        if (topicComboBox.getValue() != null) {
            previewTopicLabel.setText(topicComboBox.getValue().getName());
        } else {
            previewTopicLabel.setText("Не выбрана");
        }

        // Обновить информацию о типе
        if (typeComboBox.getValue() != null) {
            previewTypeLabel.setText(typeComboBox.getValue());
        } else {
            previewTypeLabel.setText("Не выбран");
        }

        // Обновить информацию о сложности
        RadioButton selectedRadioButton = (RadioButton) difficultyGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String difficultyText = selectedRadioButton.getText();
            previewDifficultyLabel.setText(difficultyText);

            // Установить цвет в зависимости от сложности
            switch (difficultyText) {
                case "Легкий (1)":
                    previewDifficultyLabel.setStyle("-fx-text-fill: #48bb78; -fx-font-weight: bold;");
                    break;
                case "Средний (2)":
                    previewDifficultyLabel.setStyle("-fx-text-fill: #ed8936; -fx-font-weight: bold;");
                    break;
                case "Сложный (3)":
                    previewDifficultyLabel.setStyle("-fx-text-fill: #e53e3e; -fx-font-weight: bold;");
                    break;
            }
        } else {
            previewDifficultyLabel.setText("Не выбрана");
            previewDifficultyLabel.setStyle("");
        }

        // Обновить текст вопроса
        String questionText = questionTextArea.getText().trim();
        if (!questionText.isEmpty()) {
            previewQuestionText.setText(questionText);
        } else {
            previewQuestionText.setText("Текст вопроса не введен");
        }
    }

    private void clearPreview() {
        previewSubjectLabel.setText("Не выбран");
        previewTopicLabel.setText("Не выбрана");
        previewTypeLabel.setText("Не выбран");
        previewDifficultyLabel.setText("Не выбрана");
        previewDifficultyLabel.setStyle("");
        previewQuestionText.setText("Текст вопроса не введен");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddSubject() {
        // Создаем диалоговое окно для добавления нового предмета
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавление нового предмета");
        dialog.setHeaderText("Введите название нового предмета");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Устанавливаем кнопки
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Создаем поле ввода
        TextField subjectNameField = new TextField();
        subjectNameField.setPromptText("Название предмета");
        subjectNameField.setStyle("-fx-padding: 10; -fx-font-size: 14;");

        // Создаем поле для описания
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Описание предмета (опционально)");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setStyle("-fx-padding: 5; -fx-font-size: 12;");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(
                new Label("Название предмета:"),
                subjectNameField,
                new Label("Описание:"),
                descriptionArea
        );

        dialog.getDialogPane().setContent(content);

        // Фокусируемся на поле ввода
        subjectNameField.requestFocus();

        // Преобразуем результат
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return subjectNameField.getText().trim();
            }
            return null;
        });

        // Обрабатываем результат
        dialog.showAndWait().ifPresent(subjectName -> {
            if (subjectName.isEmpty()) {
                showAlert("Ошибка", "Название предмета не может быть пустым");
                return;
            }

            try {
                // Проверяем, существует ли уже такой предмет
                Subject existingSubject = baseDAO.executeSingleQuery(
                        "FROM Subject WHERE name = :name", subjectName);

                if (existingSubject != null) {
                    showAlert("Предмет существует",
                            "Предмет с названием \"" + subjectName + "\" уже существует.");
                    return;
                }

                // Создаем новый предмет
                Subject newSubject = new Subject(subjectName, descriptionArea.getText().trim());

                // Сохраняем в базу данных
                boolean success = baseDAO.save(newSubject);

                if (success) {
                    showSuccessAlert("Предмет \"" + subjectName + "\" успешно добавлен!");

                    // Обновляем список предметов
                    refreshSubjects();

                    // Выбираем новый предмет в комбобоксе
                    subjectComboBox.getItems().stream()
                            .filter(s -> s.getName().equals(subjectName))
                            .findFirst()
                            .ifPresent(subjectComboBox::setValue);
                } else {
                    showAlert("Ошибка", "Не удалось сохранить предмет");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Произошла ошибка при добавлении предмета: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleAddTopic() {
        // Проверяем, выбран ли предмет
        Subject selectedSubject = subjectComboBox.getValue();
        if (selectedSubject == null) {
            showAlert("Ошибка", "Сначала выберите предмет для новой темы");
            return;
        }

        // Создаем диалоговое окно для добавления новой темы
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Добавление новой темы");
        dialog.setHeaderText("Введите название новой темы для предмета: " + selectedSubject.getName());
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Устанавливаем кнопки
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Создаем поле ввода
        TextField topicNameField = new TextField();
        topicNameField.setPromptText("Название темы");
        topicNameField.setStyle("-fx-padding: 10; -fx-font-size: 14;");

        // Создаем поле для описания
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Описание темы (опционально)");
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setStyle("-fx-padding: 5; -fx-font-size: 12;");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getChildren().addAll(
                new Label("Название темы:"),
                topicNameField,
                new Label("Описание:"),
                descriptionArea
        );

        dialog.getDialogPane().setContent(content);

        // Фокусируемся на поле ввода
        topicNameField.requestFocus();

        // Преобразуем результат
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return topicNameField.getText().trim();
            }
            return null;
        });

        // Обрабатываем результат
        dialog.showAndWait().ifPresent(topicName -> {
            if (topicName.isEmpty()) {
                showAlert("Ошибка", "Название темы не может быть пустым");
                return;
            }

            try {
                // Проверяем, существует ли уже такая тема для этого предмета
                Topic existingTopic = baseDAO.executeSingleQuery(
                        "FROM Topic t WHERE t.name = :name AND t.subject.id = :subjectId",
                        topicName, selectedSubject.getId());

                if (existingTopic != null) {
                    showAlert("Тема существует",
                            "Тема с названием \"" + topicName + "\" уже существует для этого предмета.");
                    return;
                }

                // Создаем новую тему
                Topic newTopic = new Topic(selectedSubject, topicName, descriptionArea.getText().trim());

                // Сохраняем в базу данных
                boolean success = baseDAO.save(newTopic);

                if (success) {
                    showSuccessAlert("Тема \"" + topicName + "\" успешно добавлена!");

                    // Обновляем список тем для выбранного предмета
                    refreshTopics(selectedSubject);

                    // Выбираем новую тему в комбобоксе
                    topicComboBox.getItems().stream()
                            .filter(t -> t.getName().equals(topicName))
                            .findFirst()
                            .ifPresent(topicComboBox::setValue);
                } else {
                    showAlert("Ошибка", "Не удалось сохранить тему");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Ошибка", "Произошла ошибка при добавлении темы: " + e.getMessage());
            }
        });
    }

    private void refreshSubjects() {
        // Сохраняем текущее выбранное значение
        Subject currentSelection = subjectComboBox.getValue();
        String currentName = currentSelection != null ? currentSelection.getName() : null;

        // Обновляем список предметов
        List<Subject> subjects = subjectDAO.getAllSubjects();
        subjectComboBox.getItems().setAll(subjects);

        // Восстанавливаем выбор
        if (currentName != null) {
            subjectComboBox.getItems().stream()
                    .filter(s -> s.getName().equals(currentName))
                    .findFirst()
                    .ifPresent(subjectComboBox::setValue);
        }
    }

    private void refreshTopics(Subject subject) {
        // Сохраняем текущее выбранное значение
        Topic currentSelection = topicComboBox.getValue();
        String currentName = currentSelection != null ? currentSelection.getName() : null;

        // Обновляем список тем
        List<Topic> topics = subjectDAO.getTopicsBySubject(subject);
        topicComboBox.getItems().setAll(topics);

        // Восстанавливаем выбор
        if (currentName != null) {
            topicComboBox.getItems().stream()
                    .filter(t -> t.getName().equals(currentName))
                    .findFirst()
                    .ifPresent(topicComboBox::setValue);
        }
    }

    // Обновляем метод handleSubjectSelected для использования обновленного списка тем
    private void handleSubjectSelected() {
        Subject selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            refreshTopics(selectedSubject);
            topicComboBox.setDisable(false);
            btnAddTopic.setDisable(false); // Активируем кнопку добавления темы
        } else {
            topicComboBox.getItems().clear();
            topicComboBox.setDisable(true);
            btnAddTopic.setDisable(true); // Деактивируем кнопку добавления темы
        }
        updatePreview();
    }



    // Обновляем метод handleClear
    @FXML
    private void handleClear() {
        // Очистить все поля
        subjectComboBox.setValue(null);
        topicComboBox.setValue(null);
        typeComboBox.setValue(null);
        difficultyGroup.selectToggle(null);
        questionTextArea.clear();
        answerTextArea.clear();

        // Деактивировать кнопку добавления темы
        btnAddTopic.setDisable(true);

        // Сбросить предпросмотр
        clearPreview();

        // Установить фокус на первое поле
        subjectComboBox.requestFocus();
    }
}