package com.examsystem.dao;

import com.examsystem.models.Subject;
import com.examsystem.models.Topic;
import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Subject ORDER BY name", Subject.class).list();
        }
    }

    public Subject getSubjectById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Subject.class, id);
        }
    }

    public void saveSubject(Subject subject) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(subject);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Topic> getTopicsBySubject(Subject subject) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Topic> query = session.createQuery(
                    "FROM Topic WHERE subject = :subject ORDER BY name", Topic.class);
            query.setParameter("subject", subject);
            return query.list();
        }
    }
}