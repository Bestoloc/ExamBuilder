package com.examsystem.controllers;

import com.examsystem.dao.QuestionDAO;
import com.examsystem.dao.StudentDAO;
import com.examsystem.dao.TicketDAO;
import com.examsystem.models.Teacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private Label questionCountLabel;
    @FXML private Label ticketCountLabel;
    @FXML private Label studentCountLabel;

    private Teacher currentTeacher;
    private QuestionDAO questionDAO = new QuestionDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private StudentDAO studentDAO = new StudentDAO();

    // Добавьте этот метод для обновления статистики
    private void updateStatistics() {
        // Получение данных из БД
        long questionCount = questionDAO.getQuestionCount();
        long ticketCount = ticketDAO.getTicketCount();
        long studentCount = studentDAO.getAllStudents().size();

        // Обновление UI
        questionCountLabel.setText(String.valueOf(questionCount));
        ticketCountLabel.setText(String.valueOf(ticketCount));
        studentCountLabel.setText(String.valueOf(studentCount));
    }

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Преподаватель: " + teacher.getFullName());
        }
        updateStatistics();
    }

    @FXML
    public void initialize() {
        // Инициализация происходит после setCurrentTeacher
    }

    private void loadStatistics() {
        long questionCount = questionDAO.getQuestionCount();
        long ticketCount = ticketDAO.getTicketCount();
        long studentCount = studentDAO.getAllStudents().size();

        questionCountLabel.setText(String.valueOf(questionCount));
        ticketCountLabel.setText(String.valueOf(ticketCount));
        studentCountLabel.setText(String.valueOf(studentCount));
    }

    @FXML
    private void handleCreateTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create.fxml"));
            Parent root = loader.load();

            CreateTicketController controller = loader.getController();
            controller.setCurrentTeacher(currentTeacher);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle("Создание билета");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно создания билета");
        }
    }

    @FXML
    private void handleAddQuestion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_question.fxml"));
            Parent root = loader.load();

            AddQuestionController controller = loader.getController();
            controller.setCurrentTeacher(currentTeacher);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle("Добавление вопроса");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно добавления вопроса");
        }
    }

    @FXML
    private void handleAnalysis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/analysis.fxml"));
            Parent root = loader.load();

            AnalysisController controller = loader.getController();
            controller.setCurrentTeacher(currentTeacher);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle("Анализ результатов");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось открыть окно анализа");
        }
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.close();

        try {
            Stage loginStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            loginStage.setScene(new Scene(root, 500, 500));
            loginStage.setTitle("Вход в систему");
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}