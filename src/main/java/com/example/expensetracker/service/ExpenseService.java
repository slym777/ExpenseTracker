package com.example.expensetracker.service;

import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.TripRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.utils.Helpers;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, TripRepository tripRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
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

    public ExpenseDto editExpense(Long expenseId, ExpenseDto expenseDto) {
        JMapper<ExpenseDto, Expense> tripMapper= new JMapper<>(
                ExpenseDto.class, Expense.class);
        var expense = expenseRepository.findExpenseById(expenseId).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expenseId", expenseId));

        if (!Helpers.IsNullOrEmpty(expenseDto.getAmount().toString()) && !Objects.equals(expenseDto.getAmount(), expense.getAmount())) {
            expense.setAmount(expenseDto.getAmount());
        }
        if (!Helpers.IsNullOrEmpty(expenseDto.getDescription()) && !Objects.equals(expenseDto.getDescription(), expense.getDescription())) {
            expense.setDescription(expenseDto.getDescription());
        }
        if (!Helpers.IsNullOrEmpty(expenseDto.getType().name()) && !Objects.equals(expenseDto.getType(), expense.getType())) {
            expense.setType(expenseDto.getType());
        }

        expenseRepository.save(expense);
        return tripMapper.getDestination(getExpenseById(expenseId));
    }

    public void deleteExpense(Long expenseId) {
        var expense = expenseRepository.findExpenseById(expenseId).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expenseId", expenseId));

        var trip = tripRepository.findTripById(expense.getTrip().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", expense.getTrip().getId()));
        expense.setTrip(trip);
        removeCreditors(expense);
        trip.getExpenses().remove(expense);
        expenseRepository.delete(expense);
    }

    public List<Expense> getCreditorExpenses(Long userId) {
        var user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        return user.getCreditorExpenses();
    }

    private void removeCreditors(Expense expense)
    {
        var creditors = new ArrayList<>(expense.getCreditors());
        creditors.forEach(us -> expense.removeCreditor(userRepository.findUserById(us.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expenseId", expense.getId())
        )));
    }

}
