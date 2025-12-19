package com.examsystem.controllers;

import com.examsystem.dao.*;
import com.examsystem.models.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateTicketController {

    // FXML элементы
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private ComboBox<Topic> topicComboBox;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private ComboBox<String> groupComboBox;
    @FXML private Spinner<Integer> questionCountSpinner;
    @FXML private VBox studentsSection;
    @FXML private TableView<StudentWrapper> studentsTable;
    @FXML private TableView<TicketWrapper> ticketsTable;
    @FXML private Button generateButton;

    // DAO объекты
    private SubjectDAO subjectDAO = new SubjectDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private QuestionDAO questionDAO = new QuestionDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private UsageDAO usageDAO = new UsageDAO();

    // Данные
    private Teacher currentTeacher;
    private ObservableList<StudentWrapper> studentWrappers = FXCollections.observableArrayList();
    private ObservableList<TicketWrapper> ticketWrappers = FXCollections.observableArrayList();

    // Классы-обертки для таблиц
    public class StudentWrapper {
        private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
        private final SimpleStringProperty fullName = new SimpleStringProperty();
        private final SimpleStringProperty groupName = new SimpleStringProperty();
        private final SimpleStringProperty topic = new SimpleStringProperty();
        private final SimpleStringProperty studentCode = new SimpleStringProperty();
        private final Student student;

        public StudentWrapper(Student student, String topicName) {
            this.student = student;
            this.fullName.set(student.getFullName());
            this.topic.set(topicName);
            this.groupName.set(student.getGroupName());
            this.studentCode.set(student.getStudentCode());
        }

        public boolean isSelected() { return selected.get(); }
        public void setSelected(boolean selected) { this.selected.set(selected); }
        public SimpleBooleanProperty selectedProperty() { return selected; }

        public String getFullName() { return fullName.get(); }
        public void setFullName(String fullName) { this.fullName.set(fullName); }
        public SimpleStringProperty fullNameProperty() { return fullName; }

        public String getGroupName() { return groupName.get(); }
        public void setGroupName(String groupName) { this.groupName.set(groupName); }
        public SimpleStringProperty groupNameProperty() { return groupName; }

        public String getStudentCode() { return studentCode.get(); }
        public void setStudentCode(String studentCode) { this.studentCode.set(studentCode); }
        public SimpleStringProperty studentCodeProperty() { return studentCode; }

        public Student getStudent() { return student; }
    }

    public class TicketWrapper {
        private final SimpleIntegerProperty ticketNumber = new SimpleIntegerProperty();
        private final SimpleStringProperty studentName = new SimpleStringProperty();
        private final SimpleStringProperty questionText = new SimpleStringProperty();
        private final SimpleStringProperty topic = new SimpleStringProperty();
        private final SimpleStringProperty difficulty = new SimpleStringProperty();
        private final SimpleDoubleProperty score = new SimpleDoubleProperty();
        private final SimpleStringProperty comment = new SimpleStringProperty();

        private Ticket ticket;
        private Question question;
        private Student student;
        private Usage usage;

        public TicketWrapper(Ticket ticket, String topicName, Student student, Question question) {
            this.ticket = ticket;
            this.student = student;
            this.question = question;

            this.ticketNumber.set(ticket.getNumber());
            this.studentName.set(student.getFullName());
            this.questionText.set(question.getText());
            this.topic.set(topicName);
            this.difficulty.set(question.getDifficultyString());
        }

        public int getTicketNumber() { return ticketNumber.get(); }
        public void setTicketNumber(int ticketNumber) { this.ticketNumber.set(ticketNumber); }
        public SimpleIntegerProperty ticketNumberProperty() { return ticketNumber; }

        public String getStudentName() { return studentName.get(); }
        public void setStudentName(String studentName) { this.studentName.set(studentName); }
        public SimpleStringProperty studentNameProperty() { return studentName; }

        public String getQuestionText() { return questionText.get(); }
        public void setQuestionText(String questionText) { this.questionText.set(questionText); }
        public SimpleStringProperty questionTextProperty() { return questionText; }

        public String getTopic() { return topic.get(); }
        public void setTopic(String topic) { this.topic.set(topic); }
        public SimpleStringProperty topicProperty() { return topic; }

        public String getDifficulty() { return difficulty.get(); }
        public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }
        public SimpleStringProperty difficultyProperty() { return difficulty; }

        public double getScore() { return score.get(); }
        public void setScore(double score) { this.score.set(score); }
        public SimpleDoubleProperty scoreProperty() { return score; }

        public String getComment() { return comment.get(); }
        public void setComment(String comment) { this.comment.set(comment); }
        public SimpleStringProperty commentProperty() { return comment; }

        public Ticket getTicket() { return ticket; }
        public Student getStudent() { return student; }
        public Question getQuestion() { return question; }
        public Usage getUsage() { return usage; }
        public void setUsage(Usage usage) { this.usage = usage; }
    }

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        initializeData();
    }

    @FXML
    public void initialize() {
        // Загрузка предметов
        List<Subject> subjects = subjectDAO.getAllSubjects();
        subjectComboBox.getItems().addAll(subjects);

        // Настройка отображения предметов
        subjectComboBox.setCellFactory(param -> new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject subject, boolean empty) {
                super.updateItem(subject, empty);
                if (empty || subject == null) {
                    setText(null);
                } else {
                    setText(subject.getName());
                }
            }
        });

        // Настройка отображения выбранного предмета
        subjectComboBox.setButtonCell(new ListCell<Subject>() {
            @Override
            protected void updateItem(Subject subject, boolean empty) {
                super.updateItem(subject, empty);
                if (empty || subject == null) {
                    setText(null);
                } else {
                    setText(subject.getName());
                }
            }
        });

        // Обработчик выбора предмета
        subjectComboBox.setOnAction(event -> {
            Subject selectedSubject = subjectComboBox.getValue();
            if (selectedSubject != null) {
                // Загрузка тем для выбранного предмета
                loadTopicsForSubject(selectedSubject);
            }
        });
    }

    private void loadTopicsForSubject(Subject subject) {
        topicComboBox.getItems().clear();
        List<Topic> topics = subjectDAO.getTopicsBySubject(subject);
        topicComboBox.getItems().addAll(topics);

        // Настройка отображения тем
        topicComboBox.setCellFactory(param -> new ListCell<Topic>() {
            @Override
            protected void updateItem(Topic topic, boolean empty) {
                super.updateItem(topic, empty);
                if (empty || topic == null) {
                    setText(null);
                } else {
                    setText(topic.getName());
                }
            }
        });

        // Настройка отображения выбранной темы
        topicComboBox.setButtonCell(new ListCell<Topic>() {
            @Override
            protected void updateItem(Topic topic, boolean empty) {
                super.updateItem(topic, empty);
                if (empty || topic == null) {
                    setText(null);
                } else {
                    setText(topic.getName());
                }
            }
        });
    }

    private void initializeData() {
        // Загрузка предметов
        subjectComboBox.getItems().setAll(subjectDAO.getAllSubjects());

        // Загрузка групп
        groupComboBox.getItems().setAll(studentDAO.getAllGroups());

        // Настройка Spinner
        questionCountSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));

        // Настройка обработчиков
        subjectComboBox.setOnAction(e -> handleSubjectSelected());
        groupComboBox.setOnAction(e -> handleGroupSelected());


        // Настройка таблицы студентов
        studentsTable.setItems(studentWrappers);

        // Получаем первую колонку (колонку выбора)
        TableColumn<StudentWrapper, Boolean> selectColumn = (TableColumn<StudentWrapper, Boolean>)
                studentsTable.getColumns().get(0);

        // Настраиваем привязку к свойству selected
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        // Настраиваем отображение CheckBox
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // Делаем таблицу редактируемой (это важно!)
        studentsTable.setEditable(true);

        // Убедитесь, что колонка также редактируема
        selectColumn.setEditable(true);

        // Настройка таблицы билетов
        ticketsTable.setItems(ticketWrappers);

        // Настройка редактируемых столбцов для оценок и комментариев
        setupEditableColumns();
        studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ticketsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupEditableColumns() {
        // Убедитесь, что таблица редактируема
        ticketsTable.setEditable(true);

        // Получаем столбец оценок (индекс 5)
        TableColumn<TicketWrapper, Double> scoreColumn = (TableColumn<TicketWrapper, Double>)
                ticketsTable.getColumns().get(5);

        // Настройка фабрики ячеек для оценок
        scoreColumn.setCellFactory(column -> new TableCell<TicketWrapper, Double>() {
            private final TextField textField = new TextField();

            {
                textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal) {
                        commitEdit(parseAndValidateScore(textField.getText()));
                    }
                });

                textField.setOnAction(e -> commitEdit(parseAndValidateScore(textField.getText())));
            }

            @Override
            public void startEdit() {
                super.startEdit();
                Double currentValue = getItem();
                textField.setText(currentValue != null ? String.format("%.1f", currentValue) : "");
                setGraphic(textField);
                setText(null);
                textField.selectAll();
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? String.format("%.1f", getItem()) : "");
                setGraphic(null);
            }

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    textField.setText(item != null ? String.format("%.1f", item) : "");
                    setGraphic(textField);
                    setText(null);
                } else {
                    setText(item != null ? String.format("%.1f", item) : "");
                    setGraphic(null);
                }
            }

            private Double parseAndValidateScore(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    return 0.0;
                }

                try {
                    Double score = Double.parseDouble(text);

                    // ВАЛИДАЦИЯ: от 2.0 до 5.0
                    if (score < 2.0) {
                        throw new IllegalArgumentException("Оценка не может быть ниже 2.0");
                    }
                    if (score > 5.0) {
                        throw new IllegalArgumentException("Оценка не может быть выше 5.0");
                    }

                    // Округление до одного знака после запятой
                    return Math.round(score * 10.0) / 10.0;

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Введите корректное число");
                }
            }
        });

        // Важная часть: настройка сохранения изменений оценок
        scoreColumn.setOnEditCommit(event -> {
            TicketWrapper wrapper = event.getRowValue();
            Double newScore = event.getNewValue();

            // Проверка диапазона оценки (например, от 0 до 100)
            if (newScore < 0) newScore = 0.0;
            if (newScore > 100) newScore = 100.0;

            wrapper.setScore(newScore);

            // Обновление строки в таблице
            ticketsTable.refresh();
        });

        // Настройка привязки данных для столбца оценок
        scoreColumn.setCellValueFactory(cellData ->
                cellData.getValue().scoreProperty().asObject());

        // Столбец комментариев (индекс 6)
        TableColumn<TicketWrapper, String> commentColumn = (TableColumn<TicketWrapper, String>)
                ticketsTable.getColumns().get(6);

        // Используем стандартную фабрику для текстовых ячеек
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Настройка сохранения комментариев
        commentColumn.setOnEditCommit(event -> {
            TicketWrapper wrapper = event.getRowValue();
            wrapper.setComment(event.getNewValue());
        });

        // Настройка привязки данных для столбца комментариев
        commentColumn.setCellValueFactory(cellData ->
                cellData.getValue().commentProperty());

        // Делаем столбцы редактируемыми
        scoreColumn.setEditable(true);
        commentColumn.setEditable(true);
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
    }

    private void handleGroupSelected() {
        String selectedGroup = groupComboBox.getValue();
        if (selectedGroup != null) {
            List<Student> students = studentDAO.getStudentsByGroup(selectedGroup);
            studentWrappers.clear();

            // Получаем выбранную тему
            Topic selectedTopic = topicComboBox.getValue();
            String topicName = (selectedTopic != null) ? selectedTopic.getName() : "Не выбрана";

            for (Student student : students) {
                // Теперь передаем два аргумента
                studentWrappers.add(new StudentWrapper(student, topicName));
            }
            studentsSection.setVisible(true);
        }
    }

    @FXML
    private void handleShowStudents() {
        String selectedGroup = groupComboBox.getValue();
        if (selectedGroup == null) {
            showAlert("Ошибка", "Выберите группу");
            return;
        }
        handleGroupSelected();
    }

    @FXML
    private void handleGenerateTicket() {
        // Проверка входных данных
        if (!validateInput()) {
            return;
        }

        Subject subject = subjectComboBox.getValue();
        Topic topic = topicComboBox.getValue();
        int difficulty = getSelectedDifficulty();
        int questionCount = questionCountSpinner.getValue();

        // Получить вопросы по теме и сложности
        List<Question> availableQuestions = questionDAO.getQuestionsBySubjectAndTopic(subject, topic);
        List<Question> filteredQuestions = new ArrayList<>();

        for (Question q : availableQuestions) {
            if (q.getDifficulty() == difficulty) {
                filteredQuestions.add(q);
            }
        }

        if (filteredQuestions.size() < questionCount) {
            showAlert("Ошибка",
                    "Недостаточно вопросов выбранной сложности. Доступно: " +
                            filteredQuestions.size() + ", требуется: " + questionCount);
            return;
        }

        // Выбрать случайные вопросы
        List<Question> selectedQuestions = selectRandomQuestions(filteredQuestions, questionCount);

        // Создать билет
        int ticketNumber = ticketDAO.getNextTicketNumber(subject);
        Ticket ticket = new Ticket(subject, ticketNumber,
                "Билет по теме: " + topic.getName(), currentTeacher);

        // === ВАЖНО: СОХРАНИТЬ билет ПЕРВЫМ ===
        ticketDAO.saveTicket(ticket);

        // === ПОТОМ добавлять вопросы ===
        for (int i = 0; i < selectedQuestions.size(); i++) {
            ticketDAO.addQuestionToTicket(ticket, selectedQuestions.get(i), i + 1);
        }

        // Создать билеты для выбранных студентов
        createTicketsForStudents(ticket, selectedQuestions);

        showSuccessAlert("Билет успешно создан! Номер билета: " + ticketNumber);
    }

    private void createTicketsForStudents(Ticket ticket, List<Question> questions) {
        for (StudentWrapper wrapper : studentWrappers) {
            if (wrapper.isSelected()) {
                // Создать использование билета
                Usage usage = new Usage(ticket, wrapper.getStudent(), wrapper.getGroupName());
                usageDAO.saveUsage(usage);

                // Получить название темы из выбранной темы
                Topic selectedTopic = topicComboBox.getValue();
                String topicName = (selectedTopic != null) ? selectedTopic.getName() : "Не выбрана";

                // Добавить вопросы в таблицу для оценки
                for (Question question : questions) {
                    // Правильный порядок аргументов:
                    // 1. ticket, 2. topicName, 3. student, 4. question
                    TicketWrapper ticketWrapper = new TicketWrapper(
                            ticket,           // 1-й аргумент: Ticket
                            topicName,        // 2-й аргумент: String topicName
                            wrapper.getStudent(), // 3-й аргумент: Student
                            question          // 4-й аргумент: Question
                    );
                    ticketWrapper.setUsage(usage);
                    ticketWrappers.add(ticketWrapper);
                }
            }
        }

        // Сбросить выбор студентов
        for (StudentWrapper wrapper : studentWrappers) {
            wrapper.setSelected(false);
        }
    }

    @FXML
    private void handleSaveScores() {
        try {
            for (TicketWrapper wrapper : ticketWrappers) {
                // Сохранить оценку за вопрос
                QuestionScore questionScore = new QuestionScore(
                        wrapper.getUsage(),
                        wrapper.getQuestion(),
                        wrapper.getScore(),
                        wrapper.getComment()
                );

                usageDAO.saveQuestionScore(questionScore);

                // Обновить средний балл в Usage
                updateAverageScore(wrapper.getUsage());
            }

            showSuccessAlert("Оценки успешно сохранены!");
            ticketWrappers.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось сохранить оценки");
        }
    }

    private void updateAverageScore(Usage usage) {
        List<QuestionScore> scores = usage.getQuestionScores();
        if (!scores.isEmpty()) {
            double total = 0;
            for (QuestionScore score : scores) {
                if (score.getScore() != null) {
                    total += score.getScore();
                }
            }
            usage.setAvgScore(total / scores.size());
            usageDAO.updateUsage(usage);
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) subjectComboBox.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        if (subjectComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите предмет");
            return false;
        }

        if (topicComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите тему");
            return false;
        }

        if (difficultyComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите сложность");
            return false;
        }

        if (groupComboBox.getValue() == null) {
            showAlert("Ошибка", "Выберите группу");
            return false;
        }

        // Проверить, что выбраны студенты
        boolean anySelected = studentWrappers.stream().anyMatch(StudentWrapper::isSelected);
        if (!anySelected) {
            showAlert("Ошибка", "Выберите хотя бы одного студента");
            return false;
        }

        return true;
    }

    private int getSelectedDifficulty() {
        String selected = difficultyComboBox.getValue();
        if (selected.contains("Легкий")) return 1;
        if (selected.contains("Средний")) return 2;
        if (selected.contains("Сложный")) return 3;
        return 2; // По умолчанию средняя сложность
    }

    private List<Question> selectRandomQuestions(List<Question> questions, int count) {
        List<Question> selected = new ArrayList<>();
        List<Question> copy = new ArrayList<>(questions);
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            if (copy.isEmpty()) break;
            int index = random.nextInt(copy.size());
            selected.add(copy.remove(index));
        }

        return selected;
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