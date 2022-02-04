package com.example.expensetracker.repository;

import com.example.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAll();
    Optional<Expense> findExpenseById(Long id);

    @Override
    @Transactional
    Expense save(Expense expense);

    @Query(value = "select * from Expenses e where e.debtor_id=:userId and e.is_group_expense=false", nativeQuery = true)
    List<Expense> getUsersPersonalExpenses(Long userId);

}

