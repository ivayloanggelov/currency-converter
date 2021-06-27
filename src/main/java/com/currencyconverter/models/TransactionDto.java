package com.currencyconverter.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TransactionDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Size(min = 2, max = 10, message = "FromCurrency should be between 2 and 10 symbols.")
    private String fromCurrency;

    @Size(min = 2, max = 10, message = "ToCurrency should be between 2 and 10 symbols.")
    private String toCurrency;

    @Positive(message = "Amount should be positive.")
    private Double amount;

    public TransactionDto() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
