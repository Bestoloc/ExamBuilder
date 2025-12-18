package ticketbuilder.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ticketbuilder.model.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            // 🔹 Основной конфигурационный файл
            configuration.configure("hibernate.cfg.xml");

            // 🔹 Регистрируем ВСЕ Entity
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Question.class);
            configuration.addAnnotatedClass(Topic.class);
            configuration.addAnnotatedClass(TicketHistory.class);
            configuration.addAnnotatedClass(User.class); // преподаватель

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("❌ Initial SessionFactory creation failed.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
