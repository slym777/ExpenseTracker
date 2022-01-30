package com.example.expensetracker.service;

import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long expenseId) {
        return expenseRepository.findExpenseById(expenseId).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expensesId", expenseId));
    }

    public List<Expense> getTripExpensesByTripId(Long tripId, Boolean isGroup) {
        return expenseRepository.findAll().stream()
                .filter(e -> Objects.equals(e.getIsGroupExpense(), isGroup))
                .filter(e -> Objects.equals(e.getTrip().getId(), tripId))
                .collect(Collectors.toList());
    }



}
