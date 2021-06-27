package com.currencyconverter.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
public abstract class AbstractGenericCrudRepository<T> extends AbstractGenericGetRepository<T> {

    private final SessionFactory sessionFactory;

    @Autowired
    public AbstractGenericCrudRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    public void delete(Integer id, Class<T> clazz) {
        T toDelete = getByField("id", id, clazz);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(toDelete);
            session.getTransaction().commit();
        }
    }

    public T create(T objectToSave) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectToSave);
            session.getTransaction().commit();
            return objectToSave;
        }
    }

    public T update(T objectToSave) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(objectToSave);
            session.getTransaction().commit();
            return objectToSave;
        }
    }

}
