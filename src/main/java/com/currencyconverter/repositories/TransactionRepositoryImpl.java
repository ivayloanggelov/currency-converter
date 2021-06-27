package com.currencyconverter.repositories;

import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionSearchParameters;
import com.currencyconverter.repositories.contracts.TransactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TransactionRepositoryImpl extends AbstractGenericCrudRepository<Transaction> implements
        TransactionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Transaction> getAll(TransactionSearchParameters tsp) {
        try (Session session = sessionFactory.openSession()) {
            String baseQuery = "";

            if (tsp != null && tsp.getId() != null) {
                baseQuery += " from Transaction where id =:id ";
            }

            if (tsp != null && tsp.getDate() != null && !baseQuery.isEmpty()) {
                baseQuery += " and transaction_date =:date ";
            } else if (tsp != null && tsp.getDate() != null) {
                baseQuery += "from Transaction where transaction_date =:date";
            } else if (tsp != null && tsp.getId() == null && tsp.getDate() == null) {
                baseQuery += "from Transaction ";
            }

            var query = session.createQuery(baseQuery, Transaction.class);

            if (tsp != null && tsp.getId() != null) {
                query.setParameter("id", tsp.getId());
            }

            if (tsp != null && tsp.getDate() != null) {
                query.setParameter("date", tsp.getDate());
            }

            return query.list();
        }
    }

    @Override
    public Transaction getById(Integer id) {
        return super.getByField("id", id, Transaction.class);
    }

    @Override
    public Transaction getByDate(LocalDate date) {
        return super.getByField("date", date, Transaction.class);
    }

    @Override
    public Transaction create(Transaction transaction) {
        return super.create(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        return super.update(transaction);
    }

    @Override
    public void delete(Integer id) {
        super.delete(id, Transaction.class);
    }
}
