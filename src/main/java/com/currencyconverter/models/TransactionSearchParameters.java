package com.currencyconverter.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TransactionSearchParameters {
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public TransactionSearchParameters(Integer id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public TransactionSearchParameters() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
