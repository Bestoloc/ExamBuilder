package com.examsystem.controllers;

import com.examsystem.dao.QuestionDAO;
import com.examsystem.dao.StudentDAO;
import com.examsystem.dao.TicketDAO;
import com.examsystem.models.Teacher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class MainController {

    public Button btnExit;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Настройка диалога
        alert.setTitle("Выход из системы");
        alert.setHeaderText("⚠️ Подтверждение выхода");
        alert.setContentText("Вы уверены, что хотите выйти из системы?\n\n" +
                "• Все несохраненные изменения будут потеряны\n" +
                "• Для повторного входа потребуется авторизация");

        // Свои кнопки с иконками
        ButtonType yesButton = new ButtonType("✅ Выйти", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("❌ Отмена", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Получить Stage для кастомизации
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Опционально: CSS стилизация
        alert.getDialogPane().getStylesheets().add("/style.css");

        // Ожидание ответа
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                performLogout();
            } else if (response == noButton) {
                // Отмена - ничего не делаем
            }
        });
    }

    private void performLogout() {
        // Логика выхода
        try {

            // 2. Закрыть текущее окно
            Stage currentStage = (Stage) btnExit.getScene().getWindow();

            // 3. Показать окно логина или закрыть приложение
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Вход в систему");
            loginStage.show();

            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Ошибка");
            errorAlert.setContentText("Не удалось выполнить выход: " + e.getMessage());
            errorAlert.showAndWait();
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