package com.examsystem.dao;

import com.examsystem.models.*;
import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class TicketDAO {

    public List<Ticket> getAllTickets() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Ticket t ORDER BY t.subject.name, t.number",
                    Ticket.class).list();
        }
    }

    public Ticket getTicketById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Ticket.class, id);
        }
    }

    public int getNextTicketNumber(Subject subject) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(
                    "SELECT MAX(number) FROM Ticket WHERE subject = :subject", Integer.class);
            query.setParameter("subject", subject);
            Integer maxNumber = query.uniqueResult();
            return (maxNumber != null) ? maxNumber + 1 : 1;
        }
    }

    public void saveTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteTicket(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Ticket> getTicketsBySubject(Subject subject) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Ticket> query = session.createQuery(
                    "FROM Ticket WHERE subject = :subject ORDER BY number", Ticket.class);
            query.setParameter("subject", subject);
            return query.list();
        }
    }

    public long getTicketCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Ticket", Long.class);
            return query.uniqueResult();
        }
    }

    public void addQuestionToTicket(Ticket ticket, Question question, int orderNum) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            TicketQuestion ticketQuestion = new TicketQuestion(ticket, question, orderNum);
            session.save(ticketQuestion);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Question> getQuestionsByTicket(Ticket ticket) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                    "SELECT tq.question FROM TicketQuestion tq WHERE tq.ticket = :ticket ORDER BY tq.orderNum",
                    Question.class);
            query.setParameter("ticket", ticket);
            return query.list();
        }
    }

    public List<Ticket> searchTickets(String searchText) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Ticket> query = session.createQuery(
                    "FROM Ticket t WHERE t.name LIKE :search OR CAST(t.number AS string) LIKE :search ORDER BY t.number",
                    Ticket.class);
            query.setParameter("search", "%" + searchText + "%");
            return query.list();
        }
    }
}