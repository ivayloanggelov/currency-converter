package com.currencyconverter.models;

public class ExchangeRateDto {

    private String fromCurrency;
    private String toCurrency;

    public ExchangeRateDto() {
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
}
