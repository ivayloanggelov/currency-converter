package com.currencyconverter.models;

import java.io.Serializable;
import java.util.Map;

public class Rate implements Serializable {
    private String success;
    private String timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;

    public Rate() {
    }

    public Rate(String success, String timestamp, String base, String date, Map<String, String> rates) {
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}