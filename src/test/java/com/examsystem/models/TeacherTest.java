package com.examsystem.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void testTeacherId() {
        Teacher teacher = new Teacher();

        // Проверяем значение по умолчанию для примитивного long
        assertEquals(0, teacher.getId());  // примитивный long по умолчанию = 0

        // Устанавливаем ID
        teacher.setId(1);
        assertEquals(1L, teacher.getId());

        teacher.setId(100);
        assertEquals(100L, teacher.getId());

        // Нельзя установить null для примитивного типа!
        // teacher.setId(null); // ОШИБКА компиляции
    }

    @Test
    void testTeacherIdWithLongType() {
        // Если бы id был Long (объектный), тест был бы таким:
        // Teacher teacher = new Teacher();
        // assertNull(teacher.getId()); // Работает только для Long
        // teacher.setId(1L);
        // assertEquals(1L, teacher.getId());
        // teacher.setId(null); // Работает только для Long
        // assertNull(teacher.getId());

        // Но у меня примитивный long, поэтому:
        Teacher teacher = new Teacher();
        assertEquals(0, teacher.getId()); // Значение по умолчанию для long
    }

    @Test
    void testTeacherCreation() {
        Teacher teacher = new Teacher();

        // Проверяем значения по умолчанию
        assertEquals(0, teacher.getId());           // long: 0
        assertNull(teacher.getUsername());          // String: null
        assertNull(teacher.getPassword());          // String: null
        assertNull(teacher.getFullName());          // String: null
        assertNull(teacher.getEmail());             // String: null

        // Устанавливаем значения
        teacher.setId(1);
        teacher.setUsername("teacher1");
        teacher.setPassword("password123");
        teacher.setFullName("Иванов Иван Иванович");
        teacher.setEmail("ivanov@university.edu");

        // Проверяем
        assertEquals(1L, teacher.getId());
        assertEquals("teacher1", teacher.getUsername());
        assertEquals("password123", teacher.getPassword());
        assertEquals("Иванов Иван Иванович", teacher.getFullName());
        assertEquals("ivanov@university.edu", teacher.getEmail());
    }

    @Test
    void testTeacherStringFields() {
        Teacher teacher = new Teacher();

        // Строковые поля могут быть null
        assertNull(teacher.getUsername());

        teacher.setUsername("teacher1");
        assertEquals("teacher1", teacher.getUsername());

        teacher.setUsername(null);
        assertNull(teacher.getUsername());

        teacher.setUsername("");
        assertEquals("", teacher.getUsername());
    }

    @Test
    void testTeacherToString() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setUsername("teacher1");
        teacher.setFullName("Иванов И.И.");

        String toString = teacher.toString();
        assertNotNull(toString);
        assertFalse(toString.isEmpty());

        // Просто выводим для информации
        System.out.println("Teacher toString(): " + toString);
    }
}