package com.example.ticketbuilder.dao;

import com.example.ticketbuilder.util.HibernateUtil;
import com.example.ticketbuilder.model.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class TicketHistoryDAO {

    private int lastTicketId;

    public List<Question> generateTicket(int studentId, int count) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Student student = session.get(Student.class, studentId);

        List<Question> questions = session.createQuery(
                        "FROM Question ORDER BY random()", Question.class)
                .setMaxResults(count)
                .list();

        lastTicketId = (int) (System.currentTimeMillis() / 1000);

        for (Question q : questions) {
            TicketHistory th = new TicketHistory();
            th.setTicketId(lastTicketId);
            th.setStudent(student);
            th.setQuestion(q);
            th.setScore(null);
            session.persist(th);
        }

        tx.commit();
        session.close();

        return questions;
    }

    public int getLastTicketId() {
        return lastTicketId;
    }

    public void updateScoreAndComment(
            int ticketId,
            int questionId,
            int score,
            String comment) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        TicketHistory th = session.createQuery(
                        "FROM TicketHistory " +
                                "WHERE ticketId = :t AND question.id = :q",
                        TicketHistory.class)
                .setParameter("t", ticketId)
                .setParameter("q", questionId)
                .getSingleResult();

        th.setScore(score);
        th.setComment(comment);

        session.merge(th);

        tx.commit();
        session.close();
    }

    public static List<Object[]> getAnalysisByStudent(int studentId) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> result = session.createQuery("""
        SELECT
            q.id,                
            q.text,              
            COUNT(th.id),        
            AVG(th.score),       
            th.comment           
        FROM TicketHistory th
        JOIN th.question q
        WHERE th.student.id = :studentId
        GROUP BY q.id, q.text, th.comment
        ORDER BY q.id
    """, Object[].class)
                .setParameter("studentId", studentId)
                .getResultList();

        session.close();
        return result;
    }

    public static List<Object[]> getGlobalAnalysis() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> result = session.createQuery("""
        SELECT
            q.id,
            q.text,
            COUNT(th.id),
            AVG(th.score)
        FROM TicketHistory th
        JOIN th.question q
        GROUP BY q.id, q.text
        ORDER BY q.id
    """, Object[].class)
                .getResultList();

        session.close();
        return result;
    }

}

