package com.examsystem.controllers;

import com.examsystem.dao.*;
import com.examsystem.models.*;
import com.examsystem.util.HibernateUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnalysisController {

    @FXML private TableView<AnalysisWrapper> analysisTable;
    @FXML
    private TableColumn<AnalysisWrapper, String> questionTextColumn;
    @FXML
    private TableColumn<AnalysisWrapper, String> topicColumn;
    @FXML
    private TableColumn<AnalysisWrapper, String> difficultyColumn;
    @FXML
    private TableColumn<AnalysisWrapper, Integer> usageCountColumn;
    @FXML
    private TableColumn<AnalysisWrapper, Double> avgScoreColumn;
    @FXML
    private TableColumn<AnalysisWrapper, Double> minScoreColumn;
    @FXML
    private TableColumn<AnalysisWrapper, Double> maxScoreColumn;
    @FXML
    private TableColumn<AnalysisWrapper, String> statusColumn;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Label avgScoreLabel;
    @FXML private Label totalQuestionsLabel;
    @FXML private Label uniqueStudentsLabel;

    // DAO объекты
    private StudentDAO studentDAO = new StudentDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();
    private UsageDAO usageDAO = new UsageDAO();
    private QuestionDAO questionDAO = new QuestionDAO();

    // Данные
    private Teacher currentTeacher;
    private ObservableList<AnalysisWrapper> analysisData = FXCollections.observableArrayList();

    // Класс-обертка для анализа
    public class AnalysisWrapper {
        private final SimpleIntegerProperty questionId = new SimpleIntegerProperty();
        private final SimpleStringProperty questionText = new SimpleStringProperty();
        private final SimpleStringProperty topic = new SimpleStringProperty();
        private final SimpleStringProperty difficulty = new SimpleStringProperty();
        private final SimpleIntegerProperty usageCount = new SimpleIntegerProperty();
        private final SimpleDoubleProperty avgScore = new SimpleDoubleProperty();
        private final SimpleDoubleProperty minScore = new SimpleDoubleProperty();
        private final SimpleDoubleProperty maxScore = new SimpleDoubleProperty();
        private final SimpleStringProperty status = new SimpleStringProperty();

        private Question question;



        public AnalysisWrapper(Question question,String topicName, long usageCount, Double avgScore,
                               Double minScore, Double maxScore) {
            this.question = question;
            this.questionId.set(question.getId());
            this.questionText.set(question.getText());
            this.topic.set(topicName);
            this.difficulty.set(question.getDifficultyString());
            this.usageCount.set((int) usageCount);
            this.avgScore.set(avgScore != null ? avgScore : 0);
            this.minScore.set(minScore != null ? minScore : 0);
            this.maxScore.set(maxScore != null ? maxScore : 0);



            // Определить статус вопроса
            if (avgScore != null) {
                if (avgScore >= 5.0) {
                    this.status.set("Отлично");
                } else if (avgScore >= 4.0) {
                    this.status.set("Нормально");
                } else if (avgScore >= 3.0) {
                    this.status.set("Требует внимания");
                } else {
                    this.status.set("Переписать!");
                }
            } else {
                this.status.set("Не использовался");
            }
        }
        // с 5 параметрами
        public AnalysisWrapper(Question question, long usageCount, Double avgScore,
                               Double minScore, Double maxScore) {
            this(question,
                    question.getTopic() != null ? question.getTopic().getName() : "Неизвестно",
                    usageCount, avgScore, minScore, maxScore);
        }

        // Getters and setters
        public int getQuestionId() { return questionId.get(); }
        public void setQuestionId(int questionId) { this.questionId.set(questionId); }
        public SimpleIntegerProperty questionIdProperty() { return questionId; }

        public String getQuestionText() { return questionText.get(); }
        public void setQuestionText(String questionText) { this.questionText.set(questionText); }
        public SimpleStringProperty questionTextProperty() { return questionText; }

        public String getTopic() { return topic.get(); }
        public void setTopic(String topic) { this.topic.set(topic); }
        public SimpleStringProperty topicProperty() { return topic; }

        public String getDifficulty() { return difficulty.get(); }
        public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }
        public SimpleStringProperty difficultyProperty() { return difficulty; }

        public int getUsageCount() { return usageCount.get(); }
        public void setUsageCount(int usageCount) { this.usageCount.set(usageCount); }
        public SimpleIntegerProperty usageCountProperty() { return usageCount; }

        public double getAvgScore() { return avgScore.get(); }
        public void setAvgScore(double avgScore) { this.avgScore.set(avgScore); }
        public SimpleDoubleProperty avgScoreProperty() { return avgScore; }

        public double getMinScore() { return minScore.get(); }
        public void setMinScore(double minScore) { this.minScore.set(minScore); }
        public SimpleDoubleProperty minScoreProperty() { return minScore; }

        public double getMaxScore() { return maxScore.get(); }
        public void setMaxScore(double maxScore) { this.maxScore.set(maxScore); }
        public SimpleDoubleProperty maxScoreProperty() { return maxScore; }

        public String getStatus() { return status.get(); }
        public void setStatus(String status) { this.status.set(status); }
        public SimpleStringProperty statusProperty() { return status; }

        public Question getQuestion() { return question; }

    }


    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        initializeData();
    }

    @FXML
    private void initialize() {
        // Настройка фабрики ячеек для столбца статуса
        TableColumn<AnalysisWrapper, String> statusColumn = (TableColumn<AnalysisWrapper, String>) analysisTable.getColumns().get(6);

        // Настраиваем кастомную фабрику ячеек для столбца статуса
        statusColumn.setCellFactory(column -> new TableCell<AnalysisWrapper, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                    setGraphic(null);
                } else {
                    setText(status);

                    // Убираем стили строки и применяем только к ячейке
                    switch (status) {
                        case "Отлично":
                            setStyle("-fx-background-color: #10b981; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                            break;
                        case "Хорошо":
                        case "Нормально":
                            setStyle("-fx-background-color: #3b82f6; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                            break;
                        case "Требует внимания":
                        case "Внимание":
                            setStyle("-fx-background-color: #f59e0b; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                            break;
                        case "Плохо":
                        case "Переписать!":
                        case "Критично":
                            setStyle("-fx-background-color: #ef4444; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                            break;
                        case "Не использовался":
                        case "Нет данных":
                            setStyle("-fx-background-color: #64748b; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                            break;
                        default:
                            setStyle("-fx-background-color: #475569; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-alignment: center; " +
                                    "-fx-padding: 4 8;");
                    }
                }
            }
        });
        analysisTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Убедитесь, что настройка привязки данных идет ПОСЛЕ cellFactory
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }

    private void initializeData() {
        // Установить даты по умолчанию (последние 30 дней)
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));

        // Загрузить студентов
        List<Student> students = studentDAO.getAllStudents();
        studentComboBox.getItems().addAll(students);

        // Загрузить предметы
        List<Subject> subjects = subjectDAO.getAllSubjects();
        subjectComboBox.getItems().addAll(subjects);

        // Настроить таблицу
        analysisTable.setItems(analysisData);

        // Добавить цветовое оформление для статусов
        analysisTable.setRowFactory(tv -> new TableRow<AnalysisWrapper>() {
            @Override
            protected void updateItem(AnalysisWrapper item, boolean empty) {
                super.updateItem(item, empty);
                // ТОЛЬКО альтернирующие цвета строк, без окрашивания по статусу
                if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #1e293b;");
                } else {
                    setStyle("-fx-background-color: #0f172a;");
                }
            }
        });
    }

    @FXML
    private void handleShowStudentStats() {
        Student selectedStudent = studentComboBox.getValue();
        Subject selectedSubject = subjectComboBox.getValue();
        if (selectedStudent == null) {
            showAlert("Ошибка", "Выберите студента для анализа");
            return;
        }

        loadStudentStatistics(selectedStudent, selectedSubject);
        showGeneralStatistics(false);
    }

    @FXML
    private void handleShowGeneralStats() {
        showGeneralStatistics(true);
    }

    private void showGeneralStatistics(boolean isGeneral) {
        // Найти столбцы по тексту или индексу
        TableColumn<AnalysisWrapper, ?> avgScoreColumn = findColumnByText("Ср. балл");
        TableColumn<AnalysisWrapper, ?> statusColumn = findColumnByText("Статус");

        if (avgScoreColumn != null) {
            avgScoreColumn.setVisible(!isGeneral); // Скрыть для общей статистики
        }

        if (statusColumn != null) {
            statusColumn.setVisible(!isGeneral); // Скрыть для общей статистики
        }

        // Загрузить соответствующие данные
        if (isGeneral) {
            loadGeneralStatistics();
        } else {
            Student selectedStudent = studentComboBox.getValue();
            Subject selectedSubject = subjectComboBox.getValue();
            if (selectedStudent != null) {
                loadStudentStatistics(selectedStudent, selectedSubject);
            }
        }
    }

    // Вспомогательный метод для поиска столбца
    private TableColumn<AnalysisWrapper, ?> findColumnByText(String text) {
        for (TableColumn<AnalysisWrapper, ?> column : analysisTable.getColumns()) {
            if (text.equals(column.getText())) {
                return column;
            }
        }
        return null;
    }

    @FXML
    public void handleResetFilters() {
        studentComboBox.setValue(null);
        subjectComboBox.setValue(null);
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusDays(30));
        analysisData.clear();
        clearStatistics();
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) studentComboBox.getScene().getWindow();
        stage.close();
    }

    private void loadStudentStatistics(Student student,Subject subject) {
        analysisData.clear();

        // Получить все использования для студента
        List<Usage> usages = usageDAO.getUsagesByStudent(student);

        if (subject != null) {
            // Нужен НОВЫЙ метод в UsageDAO с фильтром по предмету
            usages = usageDAO.getUsagesForStudentBySubject(student, subject);
        } else {
            usages = usageDAO.getUsagesByStudent(student);
        }

        if (usages.isEmpty()) {
            showAlert("Информация", "Для выбранного студента нет данных");
            return;
        }

        // Подсчитать статистику
        int totalQuestions = 0;
        double totalScore = 0;
        int scoredQuestions = 0;

        for (Usage usage : usages) {
            for (QuestionScore questionScore : usage.getQuestionScores()) {
                totalQuestions++;
                if (questionScore.getScore() != null) {
                    totalScore += questionScore.getScore();
                    scoredQuestions++;

                    // Добавить вопрос в таблицу
                    Question question = questionScore.getQuestion();
                    Double avgScore = usageDAO.getAverageScoreByQuestion(question);
                    Long usageCount = usageDAO.getUsageCountByQuestion(question);

                    // Получить мин/макс баллы
                    Double minScore = getMinScoreForQuestion(question);
                    Double maxScore = getMaxScoreForQuestion(question);

                    AnalysisWrapper wrapper = new AnalysisWrapper(
                            question, usageCount, avgScore, minScore, maxScore);
                    analysisData.add(wrapper);
                }
            }
        }

        // Обновить статистику
        double avgScore = scoredQuestions > 0 ? totalScore / scoredQuestions : 0;
        updateStatistics(avgScore, totalQuestions, 1); // 1 уникальный студент
    }

    private void loadGeneralStatistics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Запрос с JOIN для topic
            List<Object[]> results = session.createQuery(
                    "SELECT q, t.name, COUNT(qs), AVG(qs.score), " +
                            "MIN(qs.score), MAX(qs.score) " +
                            "FROM Question q " +
                            "JOIN q.topic t " +  // ← JOIN вместо FETCH
                            "LEFT JOIN q.questionScores qs " +
                            "GROUP BY q, t.name", Object[].class).list();

            List<AnalysisWrapper> wrappers = new ArrayList<>();

            for (Object[] row : results) {
                Question question = (Question) row[0];
                String topic = (String) row[1];  // ← уже загружено
                Long count = (Long) row[2];
                Double avg = (Double) row[3];
                Double min = (Double) row[4];
                Double max = (Double) row[5];

                wrappers.add(new AnalysisWrapper(question, topic,
                        count, avg, min, max));
            }

            analysisTable.getItems().setAll(wrappers);
        }
    }

    private Double getMinScoreForQuestion(Question question) {
        // В реальном приложении здесь бы был запрос к БД
        // Для простоты возвращаем null
        return null;
    }

    private Double getMaxScoreForQuestion(Question question) {
        // В реальном приложении здесь бы был запрос к БД
        // Для простоты возвращаем null
        return null;
    }

    private void updateStatistics(double avgScore, int totalQuestions, int uniqueStudents) {
        avgScoreLabel.setText(String.format("%.1f", avgScore));
        totalQuestionsLabel.setText(String.valueOf(totalQuestions));
        uniqueStudentsLabel.setText(String.valueOf(uniqueStudents));
    }

    private void clearStatistics() {
        avgScoreLabel.setText("0.0");
        totalQuestionsLabel.setText("0");
        uniqueStudentsLabel.setText("0");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}