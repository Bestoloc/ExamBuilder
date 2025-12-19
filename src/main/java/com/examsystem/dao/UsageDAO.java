package com.examsystem.dao;

import com.examsystem.dto.AnalysisDTO;
import com.examsystem.models.*;
import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UsageDAO {

    public List<Usage> getAllUsages() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Usage u ORDER BY u.usageDate DESC",
                    Usage.class).list();
        }
    }

    public List<Usage> getUsagesByStudent(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Usage u " +
                    "LEFT JOIN FETCH u.questionScores qs " +
                    "LEFT JOIN FETCH qs.question q " +   // Загружаем Question
                    "LEFT JOIN FETCH q.topic " +         // Загружаем Topic
                    "WHERE u.student = :student " +
                    "ORDER BY u.assessedAt DESC";

            return session.createQuery(hql, Usage.class)
                    .setParameter("student", student)
                    .list();
        }
    }

    public List<Usage> getUsagesByTicket(Ticket ticket) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usage> query = session.createQuery(
                    "FROM Usage WHERE ticket = :ticket ORDER BY usageDate DESC",
                    Usage.class);
            query.setParameter("ticket", ticket);
            return query.list();
        }
    }

    public List<Usage> getUsagesByDateRange(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usage> query = session.createQuery(
                    "FROM Usage WHERE usageDate BETWEEN :startDate AND :endDate ORDER BY usageDate DESC",
                    Usage.class);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        }
    }

    public void saveUsage(Usage usage) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(usage);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateUsage(Usage usage) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(usage);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void saveQuestionScore(QuestionScore questionScore) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(questionScore);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Usage> getUsagesForStudentBySubject(Student student, Subject subject) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT u FROM Usage u " +
                    "LEFT JOIN FETCH u.questionScores qs " + // Загружаем questionScores
                    "LEFT JOIN FETCH qs.question q " +       // Загружаем Question
                    "LEFT JOIN FETCH q.topic t " +          // Загружаем Topic
                    "LEFT JOIN FETCH t.subject " +          // Загружаем Subject
                    "JOIN u.ticket tk " +
                    "JOIN tk.subject s " +
                    "WHERE u.student = :student " +
                    "AND s = :subject";
            // ORDER BY удален, т.к. нет поля createdAt

            Query<Usage> query = session.createQuery(hql, Usage.class);
            query.setParameter("student", student);
            query.setParameter("subject", subject);
            return query.list();
        }
    }

    public List<AnalysisDTO> getQuestionAnalysis() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                    "SELECT q.id, q.text, t.name, q.difficulty, " +
                            "COUNT(qs), AVG(qs.score), MIN(qs.score), MAX(qs.score) " +
                            "FROM Question q " +
                            "LEFT JOIN q.topic t " +
                            "LEFT JOIN q.questionScores qs " +
                            "GROUP BY q.id, q.text, t.name, q.difficulty", Object[].class).list();

            return results.stream()
                    .map(AnalysisDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public void updateQuestionScore(QuestionScore questionScore) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(questionScore);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Double getAverageScoreByQuestion(Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(
                    "SELECT AVG(qs.score) FROM QuestionScore qs WHERE qs.question = :question",
                    Double.class);
            query.setParameter("question", question);
            return query.uniqueResult();
        }
    }

    public Long getUsageCountByQuestion(Question question) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM QuestionScore qs WHERE qs.question = :question",
                    Long.class);
            query.setParameter("question", question);
            return query.uniqueResult();
        }
    }

    public List<Object[]> getQuestionStatistics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT q, " +  // ← Возвращаем объект Question целиком
                            "COUNT(qs), " +
                            "COALESCE(AVG(qs.score), 0), " +
                            "COALESCE(MIN(qs.score), 0), " +
                            "COALESCE(MAX(qs.score), 0) " +
                            "FROM Question q " +
                            "LEFT JOIN QuestionScore qs ON qs.question = q " +
                            "GROUP BY q.id, q.text, q.type, q.difficulty " +  // ВСЕ поля Question
                            "ORDER BY COALESCE(AVG(qs.score), 0) ASC",
                    Object[].class);
            return query.list();
        }
    }

    // Исправленный метод
    public List<Object[]> getStudentStatistics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                    "SELECT s.id, s.name, s.email, " +
                            "COUNT(u), AVG(u.avgScore), MIN(u.avgScore), MAX(u.avgScore) " +
                            "FROM Student s " +
                            "LEFT JOIN Usage u ON u.student = s " +
                            "GROUP BY s.id, s.name, s.email " +
                            "ORDER BY AVG(u.avgScore) DESC NULLS LAST",
                    Object[].class);
            return query.list();
        }
    }
}