package com.currencyconverter.services;

import com.currencyconverter.exceptions.EntityNotFoundException;
import com.currencyconverter.models.Currency;
import com.currencyconverter.models.Rate;
import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionSearchParameters;
import com.currencyconverter.repositories.contracts.TransactionRepository;
import com.currencyconverter.services.contracts.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final String URLCONVETER
            = "http://data.fixer.io/api/latest?access_key=f3cc90e3483c1c5699c053931cb5ce9f&" +
            "symbols=%s,%s&format=1";

    private static final String URLGETALLCURRENCIES =
            "http://data.fixer.io/api/symbols?access_key=04641f4a8a3b875bc43f60191a456afc";

    private final TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<String, String> getAllCurrencies() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Currency> request = new HttpEntity<>(new Currency());
        ResponseEntity<Currency> response = restTemplate
                .exchange(URLGETALLCURRENCIES, HttpMethod.GET, request, Currency.class);
        return Objects.requireNonNull(response.getBody()).getSymbols();
    }

    @Override
    public List<Transaction> getAll(TransactionSearchParameters csp) {
        return repository.getAll(csp);
    }

    @Override
    public Transaction getById(Integer id) {
        return repository.getById(id);
    }

    @Override
    public Transaction getByDate(LocalDate date) {
        return repository.getByDate(date);
    }

    @Override
    public Transaction create(Transaction transaction) {
        validateCurrencies(transaction.getFromCurrency(), transaction.getToCurrency());
        Rate rate = getRate(transaction.getFromCurrency(), transaction.getToCurrency());
        Double totalAmount = round(((transaction.getAmount() / rate.getRates().get(transaction.getFromCurrency()) *
                rate.getRates().get(transaction.getToCurrency()))), 2);
        transaction.setTotalAmount(totalAmount);
        return repository.create(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        validateCurrencies(transaction.getFromCurrency(), transaction.getToCurrency());
        Rate rate = getRate(transaction.getFromCurrency(), transaction.getToCurrency());
        Double totalAmount = round(((transaction.getAmount() / rate.getRates().get(transaction.getFromCurrency()) *
                rate.getRates().get(transaction.getToCurrency()))), 2);
        transaction.setTotalAmount(totalAmount);
        return repository.update(transaction);
    }

    @Override
    public Double exchangeRate(String fromCurrency, String toCurrency) {
        validateCurrencies(fromCurrency, toCurrency);
        Rate rate = getRate(fromCurrency, toCurrency);
        return round(((1 / rate.getRates().get(fromCurrency)) * rate.getRates().get(toCurrency)), 2);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }

    @Override
    public Page<Transaction> findPaginated(Pageable pageable, TransactionSearchParameters tsp) {

        List<Transaction> allTransactions = getAll(tsp);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Transaction> list;

        if (allTransactions.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allTransactions.size());
            list = allTransactions.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), allTransactions.size());
    }

    private double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    private Rate getRate(String from, String to) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Rate> request = new HttpEntity<>(new Rate());
        ResponseEntity<Rate> response = restTemplate
                .exchange(String.format(URLCONVETER, from, to), HttpMethod.GET, request, Rate.class);
        return response.getBody();
    }

    private void validateCurrencies(String fromCurrency, String toCurrency) {
        Map<String, String> allCurrencies = getAllCurrencies();

        if (!allCurrencies.containsKey(fromCurrency)) {
            throw new EntityNotFoundException("Base currency", "name", fromCurrency);
        } else if (!allCurrencies.containsKey(toCurrency)) {
            throw new EntityNotFoundException("Target currency", "name", toCurrency);
        }
    }
}
