package com.currencyconverter.repositories.contracts;

import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionSearchParameters;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAll(TransactionSearchParameters tsp);

    Transaction getById(Integer id);

    Transaction getByDate(LocalDate date);

    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    void delete(Integer id);
}
