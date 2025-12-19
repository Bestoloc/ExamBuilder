package com.examsystem.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testTicketCreation() {
        Ticket ticket = new Ticket();

        Subject subject = new Subject();
        subject.setName("Математика");

        Teacher teacher = new Teacher();
        teacher.setFullName("Иванов И.И.");

        ticket.setSubject(subject);
        ticket.setNumber(1);
        ticket.setName("Билет по алгебре");
        ticket.setCreatedBy(teacher);
        ticket.setCreatedAt(LocalDateTime.now());

        assertEquals(subject, ticket.getSubject());
        assertEquals(1, ticket.getNumber());
        assertEquals("Билет по алгебре", ticket.getName());
        assertEquals(teacher, ticket.getCreatedBy());
        assertNotNull(ticket.getCreatedAt());
    }

    @Test
    void testTicketNumber() {
        Ticket ticket = new Ticket();

        // Номер билета - положительное число
        ticket.setNumber(1);
        assertEquals(1, ticket.getNumber());

        ticket.setNumber(100);
        assertEquals(100, ticket.getNumber());

        // Можно установить 0 или отрицательное? (зависит от бизнес-правил)
        ticket.setNumber(0);
        assertEquals(0, ticket.getNumber());
    }
}