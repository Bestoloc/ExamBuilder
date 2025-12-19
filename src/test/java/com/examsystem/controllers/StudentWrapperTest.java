package com.examsystem.controllers;

import com.examsystem.models.Student;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentWrapperTest {

    @Test
    void testStudentWrapperCreation() {
        Student student = new Student("Группа 101", "Иван Иванов", "S001");
        CreateTicketController controller = new CreateTicketController();
        CreateTicketController.StudentWrapper wrapper =
                controller.new StudentWrapper(student,null);

        assertEquals("Иван Иванов", wrapper.getFullName());
        assertEquals("Группа 101", wrapper.getGroupName());
        assertEquals("S001", wrapper.getStudentCode());
        assertFalse(wrapper.isSelected());
    }

    @Test
    void testStudentWrapperSelection() {
        Student student = new Student("Группа 101", "Иван Иванов", "S001");
        CreateTicketController controller = new CreateTicketController();
        CreateTicketController.StudentWrapper wrapper =
                controller.new StudentWrapper(student,null);

        wrapper.setSelected(true);
        assertTrue(wrapper.isSelected());

        wrapper.setSelected(false);
        assertFalse(wrapper.isSelected());
    }

    @Test
    void testStudentWrapperProperties() {
        Student student = new Student("Группа 101", "Иван Иванов", "S001");
        CreateTicketController controller = new CreateTicketController();
        CreateTicketController.StudentWrapper wrapper =
                controller.new StudentWrapper(student,null);

        // Проверка property-методов
        assertNotNull(wrapper.selectedProperty());
        assertNotNull(wrapper.fullNameProperty());
        assertNotNull(wrapper.groupNameProperty());
        assertNotNull(wrapper.studentCodeProperty());
    }
}