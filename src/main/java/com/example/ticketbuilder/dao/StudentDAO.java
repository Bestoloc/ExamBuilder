package com.example.ticketbuilder.dao;

import com.example.ticketbuilder.util.HibernateUtil;
import com.example.ticketbuilder.model.Student;
import org.hibernate.Session;

import java.util.List;

public class StudentDAO {

    public static List<Student> findAll() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Student> students =
                session.createQuery("FROM Student", Student.class).list();
        session.close();

        return students;
    }
}

