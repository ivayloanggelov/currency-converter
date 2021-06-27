package com.currencyconverter.services.modelmappers;

import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionDto;
import com.currencyconverter.repositories.contracts.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

@Component
public class TransactionModelMapper {

    private final TransactionRepository repository;

    @Autowired
    public TransactionModelMapper(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(transaction.getDate());
        transactionDto.setFromCurrency(transaction.getFromCurrency());
        transactionDto.setToCurrency(transaction.getToCurrency());
        transactionDto.setAmount(transaction.getAmount());
        return transactionDto;
    }

    public Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        dtoToObject(transactionDto, transaction);
        return transaction;
    }

    public Transaction fromDto(TransactionDto transactionDto, Integer id) {
        Transaction transaction = repository.getById(id);
        dtoToObjectUpdate(transactionDto, transaction);
        return transaction;
    }

    private void dtoToObject(TransactionDto transactionDto, Transaction transaction) {
        transaction.setDate(LocalDate.now());
        transaction.setFromCurrency(transactionDto.getFromCurrency().toUpperCase(Locale.ROOT));
        transaction.setToCurrency(transactionDto.getToCurrency().toUpperCase(Locale.ROOT));
        transaction.setAmount(transactionDto.getAmount());
    }

    private void dtoToObjectUpdate(TransactionDto transactionDto, Transaction transaction) {
        transaction.setDate(transactionDto.getDate());
        transaction.setFromCurrency(transactionDto.getFromCurrency().toUpperCase(Locale.ROOT));
        transaction.setToCurrency(transactionDto.getToCurrency().toUpperCase(Locale.ROOT));
        transaction.setAmount(transactionDto.getAmount());
    }
}
