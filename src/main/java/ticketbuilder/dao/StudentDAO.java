package ticketbuilder.dao;

import ticketbuilder.util.HibernateUtil;
import ticketbuilder.model.Student;
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

