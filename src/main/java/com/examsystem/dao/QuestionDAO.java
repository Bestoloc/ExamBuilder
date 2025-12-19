package com.examsystem.dao;

import com.examsystem.models.Question;
import com.examsystem.models.Subject;
import com.examsystem.models.Topic;
import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class QuestionDAO {

    public List<Question> getAllQuestions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Question q ORDER BY q.subject.name, q.topic.name, q.difficulty",
                    Question.class).list();
        }
    }

    public List<Question> getQuestionsBySubjectAndTopic(Subject subject, Topic topic) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Правильный запрос: фильтруем через topic.subject
            String hql = "FROM Question q " +
                    "WHERE q.topic = :topic " +
                    "AND q.topic.subject = :subject " +  // Фильтр по предмету через тему
                    "ORDER BY q.difficulty";

            Query<Question> query = session.createQuery(hql, Question.class);
            query.setParameter("topic", topic);
            query.setParameter("subject", subject);
            return query.list();
        }
    }

    public List<Question> getQuestionsByDifficulty(int difficulty) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Question> query = session.createQuery(
                    "FROM Question WHERE difficulty = :difficulty", Question.class);
            query.setParameter("difficulty", difficulty);
            return query.list();
        }
    }

    public void saveQuestion(Question question) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(question);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public long getQuestionCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Question", Long.class);
            return query.uniqueResult();
        }
    }
}