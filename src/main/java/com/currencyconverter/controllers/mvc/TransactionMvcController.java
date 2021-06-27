package com.currencyconverter.controllers.mvc;

import com.currencyconverter.exceptions.EntityNotFoundException;
import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionDto;
import com.currencyconverter.models.TransactionSearchParameters;
import com.currencyconverter.models.ExchangeRateDto;
import com.currencyconverter.services.contracts.TransactionService;
import com.currencyconverter.services.modelmappers.TransactionModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class TransactionMvcController {

    private final TransactionService service;
    private final TransactionModelMapper modelMapper;

    @Autowired
    public TransactionMvcController(TransactionService service, TransactionModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("currencies")
    public Map<String, String> allCurrencies() {
        return service.getAllCurrencies();
    }

    @GetMapping("/transactions")
    public String showAll(Model model, @RequestParam("page") Optional<Integer> page,
                          @RequestParam("size") Optional<Integer> size,
                          @ModelAttribute("transactionSearchParameters") TransactionSearchParameters tsp) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Transaction> transactionPage = service.findPaginated(PageRequest.of(currentPage - 1, pageSize), tsp);
        List<Transaction> allTransactions = transactionPage.getContent();

        model.addAttribute("transactionsList", allTransactions);

        int totalPages = transactionPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("transactionPage", transactionPage);
        }

        return "transactions";
    }

    @GetMapping("/{id}")
    public String showSingleTransaction(@PathVariable Integer id, Model model) {
        try {
            Transaction transaction = service.getById(id);
            model.addAttribute("transaction", transaction);
        } catch (EntityNotFoundException e) {
            return "redirect:/transactions";
        }

        return "transaction";
    }

    @GetMapping("/search")
    public String handleSearch() {
        return "redirect:/transactions";
    }

    @PostMapping("/search")
    public String handleTransactionSearch(Model model, @ModelAttribute("transactionSearchParameters")
            TransactionSearchParameters transactionSearchParameters) {
        return showAll(model, Optional.empty(), Optional.empty(), transactionSearchParameters);
    }

    @GetMapping
    public String showConvertPage(Model model) {
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction-new";
    }

    @PostMapping
    public String handleConvertPage(@Valid @ModelAttribute("transactionDto") TransactionDto transactionDto,
                                    BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "transaction-new";
        }

        Transaction transaction = modelMapper.fromDto(transactionDto);
        service.create(transaction);
        model.addAttribute("transaction", transaction);
        model.addAttribute("totalAmount", transaction.getTotalAmount());
        return "transaction-new";
    }

    @GetMapping("/{id}/update")
    public String showUpdateTransactionPage(@PathVariable Integer id, Model model) {
        Transaction transaction = service.getById(id);
        TransactionDto transactionDto = modelMapper.toDto(transaction);
        model.addAttribute("transaction", transactionDto);
        return "transaction-update";
    }

    @PostMapping("/{id}/update")
    public String updateTransaction(@PathVariable Integer id,
                                    @Valid @ModelAttribute("transaction") TransactionDto transactionDto,
                                    BindingResult errors) {
        if (errors.hasErrors()) {
            return "transaction-update";
        }

        Transaction transaction = modelMapper.fromDto(transactionDto, id);
        service.update(transaction);

        return "redirect:/transactions";
    }

    @GetMapping("/exchange-rate")
    public String showExchangeRatePage(Model model) {
        model.addAttribute("exchangeRateDto", new ExchangeRateDto());
        return "exchange-rate";
    }

    @PostMapping("/exchange-rate")
    public String handleExchangeRatePage(@ModelAttribute("exchangeRateDto") ExchangeRateDto exchangeRateDto,
                                         Model model,
                                         BindingResult errors) {
        if (errors.hasErrors()) {
            return "exchange-rate";
        }

        try {
            Double exchangeRate =
                    service.exchangeRate(exchangeRateDto.getFromCurrency().toUpperCase(Locale.ROOT),
                            exchangeRateDto.getToCurrency().toUpperCase(Locale.ROOT));
            model.addAttribute("exchangeRate", exchangeRate);
            return "exchange-rate";
        } catch (EntityNotFoundException e) {

            if (e.getMessage().contains("Base")) {
                errors.rejectValue("fromCurrency", "invalid base currency", e.getMessage());
            } else {
                errors.rejectValue("toCurrency", "invalid target currency", e.getMessage());
            }

            return "exchange-rate";
        }
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/transactions";
    }
}
