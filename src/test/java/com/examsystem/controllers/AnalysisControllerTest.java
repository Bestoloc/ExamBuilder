package com.examsystem.controllers;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisControllerTest {

    @Test
    void testAnalysisControllerCreation() {
        // Тестируем сам контроллер
        AnalysisController controller = new AnalysisController();
        assertNotNull(controller);

        // Проверяем, что у контроллера есть необходимые методы
        // Используем getDeclaredMethods() для получения ВСЕХ методов (включая приватные)
        Class<?> controllerClass = controller.getClass();

        // Получаем все методы класса
        Method[] methods = controllerClass.getDeclaredMethods();

        // Собираем имена всех методов
        Set<String> methodNames = new HashSet<>();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }

        // Проверяем наличие нужных методов
        assertTrue(methodNames.contains("initialize"),
                "Метод 'initialize' должен существовать");
        assertTrue(methodNames.contains("setCurrentTeacher"),
                "Метод 'setCurrentTeacher' должен существовать");
        assertTrue(methodNames.contains("initializeData"),
                "Метод 'initializeData' должен существовать");
        assertTrue(methodNames.contains("handleShowStudentStats"),
                "Метод 'handleShowStudentStats' должен существовать");
        assertTrue(methodNames.contains("handleShowGeneralStats"),
                "Метод 'handleShowGeneralStats' должен существовать");
        assertTrue(methodNames.contains("showGeneralStatistics"),
                "Метод 'showGeneralStatistics' должен существовать");
        assertTrue(methodNames.contains("loadStudentStatistics"),
                "Метод 'loadStudentStatistics' должен существовать");
        assertTrue(methodNames.contains("loadGeneralStatistics"),
                "Метод 'loadGeneralStatistics' должен существовать");
        assertTrue(methodNames.contains("updateStatistics"),
                "Метод 'clearStatistics' должен существовать");
        assertTrue(methodNames.contains("clearStatistics"),
                "Метод 'loadStudentStatistics' должен существовать");
    }

    @Test
    void testAnalysisDataStructure() {
        AnalysisController controller = new AnalysisController();

        // Проверяем, что данные инициализированы
        // (это зависит от вашей реализации)
        try {
            // Если у вас есть метод getAnalysisData() или поле analysisData
            var field = controller.getClass().getDeclaredField("analysisData");
            field.setAccessible(true);
            var data = field.get(controller);

            assertNotNull(data, "Список analysisData должен быть инициализирован");

        } catch (Exception e) {
            System.out.println("Не удалось проверить analysisData: " + e.getMessage());
        }
    }
}