package com.example.expensetracker.controller;


import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping("/test")
    public String testConnection() {
        return "Healthy expense API call!";
    }

    @GetMapping("/allExpenses")
    public List<Expense> getAllExpenses(){
        return expenseService.getAllExpenses();
    }

    @GetMapping("/getExpense/{expenseId}")
    public Expense getExpenseById(@PathVariable Long expenseId){
        return expenseService.getExpenseById(expenseId);
    }

    @GetMapping("/getTripExpenses/{tripId}/{isGroup}")
    public List<Expense> getTripExpensesByTripId(@PathVariable Long tripId, @PathVariable Boolean isGroup){
        return expenseService.getTripExpensesByTripId(tripId, isGroup);
    }

}