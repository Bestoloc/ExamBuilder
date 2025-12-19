package com.examsystem.dao;

import com.examsystem.models.Student;
import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student ORDER BY groupName, fullName", Student.class).list();
        }
    }

    public List<Student> getStudentsByGroup(String groupName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery(
                    "FROM Student WHERE groupName = :groupName ORDER BY fullName", Student.class);
            query.setParameter("groupName", groupName);
            return query.list();
        }
    }

    public List<String> getAllGroups() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<String> query = session.createQuery(
                    "SELECT DISTINCT groupName FROM Student ORDER BY groupName", String.class);
            return query.list();
        }
    }

    public void saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}