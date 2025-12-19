package com.examsystem.controllers;

import com.examsystem.dao.QuestionDAO;
import com.examsystem.dao.SubjectDAO;
import com.examsystem.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;

public class AddQuestionController {

    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private ComboBox<Topic> topicComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ToggleGroup difficultyGroup;
    @FXML private TextArea questionTextArea;
    @FXML private TextArea answerTextArea;
    @FXML private Button saveButton;

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

    private void initializeData() {
        // Загрузить предметы
        List<Subject> subjects = subjectDAO.getAllSubjects();
        subjectComboBox.getItems().addAll(subjects);

        // Установить обработчик выбора предмета
        subjectComboBox.setOnAction(e -> handleSubjectSelected());

        // Установить обработчик для текста вопроса
        questionTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 500) {
                questionTextArea.setText(oldVal);
            }
        });
    }

    private void handleSubjectSelected() {
        Subject selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            List<Topic> topics = subjectDAO.getTopicsBySubject(selectedSubject);
            topicComboBox.getItems().setAll(topics);
            topicComboBox.setDisable(false);
        } else {
            topicComboBox.getItems().clear();
            topicComboBox.setDisable(true);
        }
        updatePreview();
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
    private void handleClear() {
        // Очистить все поля
        subjectComboBox.setValue(null);
        topicComboBox.setValue(null);
        typeComboBox.setValue(null);
        difficultyGroup.selectToggle(null);
        questionTextArea.clear();
        answerTextArea.clear();

        // Сбросить предпросмотр
        clearPreview();

        // Установить фокус на первое поле
        subjectComboBox.requestFocus();
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
}