package com.currencyconverter.repositories;

import com.currencyconverter.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.String.format;

@Transactional
public abstract class AbstractGenericGetRepository<T> {

    private final SessionFactory sessionFactory;

    public AbstractGenericGetRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <V> T getByField(String fieldName, V fieldValue, Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = format("from %s where %s = :value", clazz.getName(), fieldName);
            Query<T> query = session.createQuery(queryString, clazz);
            query.setParameter("value", fieldValue);
            return query.uniqueResultOptional().orElseThrow(() -> new EntityNotFoundException(
                    clazz.getSimpleName(), fieldName, fieldValue.toString()));
        }
    }

    public List<T> getAll(Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s ", clazz.getName()), clazz).list();
        }
    }
}
