package com.currencyconverter.services;

import com.currencyconverter.TransactionHelper;
import com.currencyconverter.exceptions.EntityNotFoundException;
import com.currencyconverter.models.Transaction;
import com.currencyconverter.models.TransactionSearchParameters;
import com.currencyconverter.repositories.contracts.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTests {

    @Mock
    TransactionRepository repository;

    @InjectMocks
    TransactionServiceImpl service;

    @Test
    public void getAllCurrencies_Return_AllCurrencies() {
        assertThat(service.getAllCurrencies().size(), equalTo(168));
    }

    @Test
    public void getAll_Should_Call_Repository() {

        TransactionSearchParameters searchParameters = new TransactionSearchParameters();

        when(repository.getAll(searchParameters)).thenReturn(new ArrayList<>());

        service.getAll(searchParameters);

        verify(repository, times(1)).getAll(searchParameters);
    }

    @Test
    public void getById_Should_Call_Repository_WhenTransactionExists() {
        var mockTransaction = TransactionHelper.createMockConversion();

        when(repository.getById(mockTransaction.getId())).thenReturn(mockTransaction);

        service.getById(mockTransaction.getId());

        verify(repository, times(1)).getById(mockTransaction.getId());
    }

    @Test
    public void getById_Should_Throw_WhenTransactionDoesNotExists() {
        var mockTransaction = TransactionHelper.createMockConversion();

        when(repository.getById(mockTransaction.getId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> service.getById(mockTransaction.getId()));
    }

    @Test
    public void getByDate_Should_Call_Repository_WhenTransactionExists() {
        var mockTransaction = TransactionHelper.createMockConversion();

        when(repository.getByDate(mockTransaction.getDate())).thenReturn(mockTransaction);

        service.getByDate(mockTransaction.getDate());

        verify(repository, times(1)).getByDate(mockTransaction.getDate());
    }

    @Test
    public void getByDate_Should_Throw_WhenTransactionDoesNotExists() {
        var mockTransaction = TransactionHelper.createMockConversion();

        when(repository.getByDate(mockTransaction.getDate())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> service.getByDate(mockTransaction.getDate()));
    }

    @Test
    public void create_Should_Call_Repository_When_TransactionIsValid() {
        var mockTransaction = TransactionHelper.createMockConversion();

        service.create(mockTransaction);

        verify(repository, times(1)).create(mockTransaction);
    }

    @Test
    public void create_Should_Throw_When_FromCurrencyDoesNotExist() {
        var mockTransaction = TransactionHelper.createMockConversion();
        mockTransaction.setFromCurrency("test");

        assertThrows(EntityNotFoundException.class,
                () -> service.create(mockTransaction));
    }

    @Test
    public void create_Should_Throw_When_ToCurrencyDoesNotExist() {
        var mockTransaction = TransactionHelper.createMockConversion();
        mockTransaction.setToCurrency("test");

        assertThrows(EntityNotFoundException.class,
                () -> service.create(mockTransaction));
    }

    @Test
    public void update_Should_Call_Repository_When_TransactionIsValid() {
        var mockTransaction = TransactionHelper.createMockConversion();

        service.update(mockTransaction);

        verify(repository, times(1)).update(mockTransaction);
    }

    @Test
    public void update_Should_Throw_When_FromCurrencyDoesNotExist() {
        var mockTransaction = TransactionHelper.createMockConversion();
        mockTransaction.setFromCurrency("test");

        assertThrows(EntityNotFoundException.class,
                () -> service.update(mockTransaction));
    }

    @Test
    public void update_Should_Throw_When_ToCurrencyDoesNotExist() {
        var mockTransaction = TransactionHelper.createMockConversion();
        mockTransaction.setToCurrency("test");

        assertThrows(EntityNotFoundException.class,
                () -> service.update(mockTransaction));
    }

    @Test
    public void exchangeRate_Should_Return_RoundedValue() {

        assertEquals(0.51, service.exchangeRate("BGN", "EUR"));
    }

    @Test
    public void delete_Should_Call_Repository() {
        service.delete(1);
        verify(repository, times(1)).delete(1);
    }

    @Test
    public void findPaginated_Should_Return_Pages() {
        Page<Transaction> page = service.findPaginated(PageRequest.of(1, 1), new TransactionSearchParameters());

        assertThat(page.getSize(), equalTo(1));
    }
}
