package com.example.expensetracker.service;

import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
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

    public List<Expense> getCreditorExpenses(Long userId) {
        var user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        return user.getCreditorExpenses();
    }



}
