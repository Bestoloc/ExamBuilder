package ticketbuilder.dao;

import ticketbuilder.model.Question;
import ticketbuilder.model.Student;
import ticketbuilder.model.TicketHistory;
import ticketbuilder.util.HibernateUtil;
import ticketbuilder.model.*;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TicketHistoryDAO {

    private int lastTicketId;

    public static TicketResult generateTicket(Student student, Topic topic) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        int ticketId = (int) (System.currentTimeMillis() / 1000);

        List<Question> questions = new ArrayList<>();

        for (int difficulty = 1; difficulty <= 3; difficulty++) {

            Question q = session.createQuery(
                            """
                            FROM Question
                            WHERE topic = :topic
                              AND difficulty = :diff
                            ORDER BY random()
                            """,
                            Question.class
                    )
                    .setParameter("topic", topic)
                    .setParameter("diff", difficulty)
                    .setMaxResults(1)
                    .uniqueResult();

            if (q == null) {
                tx.rollback();
                session.close();
                throw new RuntimeException(
                        "Недостаточно вопросов сложности " + difficulty
                );
            }

            questions.add(q);

            TicketHistory th = new TicketHistory();
            th.setTicketId(ticketId);
            th.setStudent(student);
            th.setQuestion(q);

            session.persist(th);
        }

        tx.commit();
        session.close();

        return new TicketResult(ticketId, questions);
    }


    public int getLastTicketId() {
        return lastTicketId;
    }

    /*public void updateScoreAndComment(
            int ticketId,
            int questionId,
            int score,
            String comment) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<TicketHistory> list = session.createQuery(
                        "FROM TicketHistory " +
                                "WHERE ticketId = :t AND question.id = :q",
                        TicketHistory.class
                )
                .setParameter("t", ticketId)
                .setParameter("q", questionId)
                .list();

        if (list.isEmpty()) {
            tx.rollback();
            session.close();
            throw new RuntimeException("Запись TicketHistory не найдена");
        }

        TicketHistory th = list.get(0);
        th.setScore(score);
        th.setComment(comment);

        session.merge(th);

        tx.commit();
        session.close();
    }*/


    public static List<AnalysisRow> getAnalysisByStudent(Student student) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> rows = session.createQuery(
                        """
                        SELECT
                            q.id,
                            q.text,
                            q.topic.name,
                            q.difficulty,
                            COUNT(th.id),
                            AVG(th.score),
                            th.comment
                        FROM TicketHistory th
                        JOIN th.question q
                        WHERE th.student = :student
                        GROUP BY q.id, q.text, th.comment, q.topic.name, q.difficulty
                        ORDER BY q.id
                        """,
                        Object[].class
                )
                .setParameter("student", student)
                .list();

        session.close();

        return rows.stream()
                .map(r -> new AnalysisRow(
                        (Integer) r[0],
                        (String) r[1],
                        (String) r[2],
                        (Integer) r[3],
                        (Long) r[4],
                        (Double) r[5],
                        (String) r[6]
                ))
                .toList();
    }


    public static List<Object[]> getGlobalAnalysis() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> rows = session.createQuery("""
        SELECT
            q.id,
            q.text,
            q.topic.name,
            q.difficulty,
            COUNT(th.id),
            AVG(th.score)
        FROM TicketHistory th
        JOIN th.question q
        GROUP BY q.id, q.text, q.topic.name, q.difficulty
        ORDER BY q.id
    """, Object[].class).list();


        session.close();
        return rows;
    }

    public static List<TicketHistory> findByTicketId(int ticketId) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<TicketHistory> list = session.createQuery(
                        "FROM TicketHistory WHERE ticketId = :ticketId",
                        TicketHistory.class
                )
                .setParameter("ticketId", ticketId)
                .list();

        session.close();
        return list;
    }

    public static void update(TicketHistory history) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.merge(history);

        tx.commit();
        session.close();
    }



}

