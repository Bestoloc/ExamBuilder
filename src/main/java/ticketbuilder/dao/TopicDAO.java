package ticketbuilder.dao;

import ticketbuilder.model.Topic;
import ticketbuilder.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class TopicDAO {

    public static List<Topic> findAll() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Topic> topics = session
                .createQuery("FROM Topic ORDER BY name", Topic.class)
                .list();

        session.close();
        return topics;
    }

    public Topic findById(int id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Topic topic = session.get(Topic.class, id);
        session.close();

        return topic;
    }
}
