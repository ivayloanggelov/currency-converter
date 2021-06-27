package com.currencyconverter.models;

import java.io.Serializable;
import java.util.Map;

public class Currency implements Serializable {
    /**
     * Class, whose objects take response from API data.fixer.io/api/
     */
    private String success;
    private Map<String, String> symbols;

    public Currency() {
    }

    public Currency(String success, Map<String, String> symbols) {
        this.success = success;
        this.symbols = symbols;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols) {
        this.symbols = symbols;
    }
}