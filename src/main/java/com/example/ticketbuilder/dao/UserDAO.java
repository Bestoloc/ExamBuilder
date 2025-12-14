package com.example.ticketbuilder.dao;

import com.example.ticketbuilder.util.HibernateUtil;
import com.example.ticketbuilder.model.User;
import org.hibernate.Session;

public class UserDAO {

    public User login(String login, String password) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM User WHERE login = :l AND password = :p",
                            User.class)
                    .setParameter("l", login)
                    .setParameter("p", password)
                    .uniqueResult();
        }
    }
}
