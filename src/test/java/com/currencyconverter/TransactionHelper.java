package com.currencyconverter;

import com.currencyconverter.models.Transaction;

import java.time.LocalDate;

public class TransactionHelper {

    public static Transaction createMockConversion() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setDate(LocalDate.now());
        transaction.setFromCurrency("BGN");
        transaction.setToCurrency("EUR");
        transaction.setAmount(10.0);
        return transaction;
    }
}
