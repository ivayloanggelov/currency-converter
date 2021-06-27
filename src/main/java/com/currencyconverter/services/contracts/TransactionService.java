package com.currencyconverter.services.contracts;

import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    List<Transaction> getAll(TransactionSearchParameters tsp);

    Map<String, String> getAllCurrencies();

    Transaction getById(Integer id);

    Transaction getByDate(LocalDate date);

    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    Double exchangeRate(String fromCurrency, String toCurrency);

    void delete(Integer id);

    Page<Transaction> findPaginated(Pageable pageable, TransactionSearchParameters tsp);
}
