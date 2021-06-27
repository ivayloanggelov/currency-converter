package com.currencyconverter.controllers.rest;

import com.currencyconverter.exceptions.EntityNotFoundException;
import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionDto;
import com.currencyconverter.models.TransactionSearchParameters;
import com.currencyconverter.services.contracts.TransactionService;
import com.currencyconverter.services.modelmappers.TransactionModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionModelMapper modelMapper;
    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionModelMapper modelMapper, TransactionService service) {
        this.modelMapper = modelMapper;
        this.service = service;
    }

    @GetMapping
    public List<Transaction> allConversions(@RequestParam(required = false) Integer id,
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                            @RequestParam(required = false) LocalDate date) {
        return service.getAll(new TransactionSearchParameters(id, date));
    }

    @GetMapping("/allCurrencies")
    public Map<String, String> getAllCurrencies() {
        return service.getAllCurrencies();
    }

    @GetMapping("/{id}")
    public Transaction getById(@PathVariable Integer id) {
        try {
            return service.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/date")
    public Transaction getByDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date) {
        try {
            return service.getByDate(date);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/exchange-rate")
    public Double exchangeRate(@RequestParam String fromCurrency, @RequestParam String toCurrency) {
        return service.exchangeRate(fromCurrency.toUpperCase(Locale.ROOT), toCurrency.toUpperCase(Locale.ROOT));
    }

    @PostMapping("/convert")
    public Transaction convert(@Valid @RequestBody TransactionDto transactionDto) {
        try {
            Transaction transaction = modelMapper.fromDto(transactionDto);
            return service.create(transaction);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Transaction update(@Valid @RequestBody TransactionDto transactionDto, @PathVariable Integer id) {
        try {
            Transaction transaction = modelMapper.fromDto(transactionDto, id);
            return service.update(transaction);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}