package com.examsystem.dao;

import com.examsystem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Базовый DAO класс с общими операциями CRUD
 * Отвечает за сохранение всех сущностей в БД
 */
public class BaseDAO {

    /**
     * Сохранить новую сущность в БД
     * @param entity сущность для сохранения
     * @return true если успешно, false если ошибка
     */
    public <T> boolean save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка сохранения сущности: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Обновить существующую сущность в БД
     * @param entity сущность для обновления
     * @return true если успешно, false если ошибка
     */
    public <T> boolean update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка обновления сущности: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Сохранить или обновить сущность (merge)
     * @param entity сущность для сохранения/обновления
     * @return true если успешно, false если ошибка
     */
    public <T> boolean saveOrUpdate(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка saveOrUpdate сущности: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удалить сущность из БД
     * @param entity сущность для удаления
     * @return true если успешно, false если ошибка
     */
    public <T> boolean delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка удаления сущности: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удалить сущность по ID
     * @param entityClass класс сущности
     * @param id ID сущности
     * @return true если успешно, false если ошибка
     */
    public <T, ID extends Serializable> boolean deleteById(Class<T> entityClass, ID id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                System.err.println("Сущность с ID " + id + " не найдена");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка удаления сущности по ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Получить сущность по ID
     * @param entityClass класс сущности
     * @param id ID сущности
     * @return сущность или null если не найдена
     */
    public <T, ID extends Serializable> T getById(Class<T> entityClass, ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            System.err.println("Ошибка получения сущности по ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Получить все сущности указанного класса
     * @param entityClass класс сущности
     * @return список сущностей
     */
    public <T> List<T> getAll(Class<T> entityClass) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName() + " e";
            return session.createQuery(hql, entityClass).list();
        } catch (Exception e) {
            System.err.println("Ошибка получения всех сущностей: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Выполнить произвольный HQL запрос
     * @param hql HQL запрос
     * @param params параметры запроса
     * @return результат запроса
     */
    public <T> List<T> executeQuery(String hql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(hql);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }

            return query.list();
        } catch (Exception e) {
            System.err.println("Ошибка выполнения запроса: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Выполнить произвольный HQL запрос с одним результатом
     * @param hql HQL запрос
     * @param params параметры запроса
     * @return результат запроса или null
     */
    public <T> T executeSingleQuery(String hql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery(hql);

            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }

            return query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Ошибка выполнения одиночного запроса: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Выполнить операцию в транзакции
     * @param operation операция для выполнения
     * @return true если успешно, false если ошибка
     */
    public boolean executeInTransaction(Consumer<Session> operation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            operation.accept(session);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка выполнения транзакции: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Выполнить операцию в транзакции с возвратом результата
     * @param operation операция для выполнения
     * @return результат операции или null при ошибке
     */
    public <R> R executeInTransactionWithResult(Function<Session, R> operation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            R result = operation.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка выполнения транзакции с результатом: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Сохранить несколько сущностей в одной транзакции
     * @param entities список сущностей для сохранения
     * @return true если успешно, false если ошибка
     */
    public <T> boolean saveAll(List<T> entities) {
        return executeInTransaction(session -> {
            for (T entity : entities) {
                session.save(entity);
            }
        });
    }

    /**
     * Обновить несколько сущностей в одной транзакции
     * @param entities список сущностей для обновления
     * @return true если успешно, false если ошибка
     */
    public <T> boolean updateAll(List<T> entities) {
        return executeInTransaction(session -> {
            for (T entity : entities) {
                session.update(entity);
            }
        });
    }
}