package com.example.expensetracker.controller;


import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/editExpense/{expenseId}")
    public ExpenseDto editExpense(@PathVariable Long expenseId, @RequestBody ExpenseDto expenseDto){
        return expenseService.editExpense(expenseId, expenseDto);
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId) {
        expenseService.DeleteExpense(expenseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getTripExpenses/{tripId}/{isGroup}")
    public List<Expense> getTripExpensesByTripId(@PathVariable Long tripId, @PathVariable Boolean isGroup){
        return expenseService.getTripExpensesByTripId(tripId, isGroup);
    }

    @GetMapping("/getCreditorExpenses/{userId}")
    public ResponseEntity<?> getCreditorExpensesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok().body(expenseService.getCreditorExpenses(userId));
    }

}