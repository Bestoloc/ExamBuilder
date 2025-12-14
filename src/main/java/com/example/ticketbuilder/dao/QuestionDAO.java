package com.example.ticketbuilder.dao;

import com.example.ticketbuilder.util.HibernateUtil;
import com.example.ticketbuilder.model.Question;
import org.hibernate.Session;

import java.util.List;

public class QuestionDAO {

    public List<Question> getRandom(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Question ORDER BY random()",
                    Question.class
            ).setMaxResults(limit).list();
        }
    }
}
