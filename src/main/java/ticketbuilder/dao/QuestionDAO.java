package ticketbuilder.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ticketbuilder.model.Question;
import ticketbuilder.util.HibernateUtil;

public class QuestionDAO {

    public static Question getRandomQuestionByDifficulty(int topicId, int difficulty) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Question q = session.createQuery("""
        FROM Question
        WHERE topic.id = :topicId
          AND difficulty = :diff
        ORDER BY random()
    """, Question.class)
                .setParameter("topicId", topicId)
                .setParameter("diff", difficulty)
                .setMaxResults(1)
                .uniqueResult();

        session.close();
        return q;
    }

    public void save(Question question) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.persist(question);

        tx.commit();
        session.close();
    }
}

