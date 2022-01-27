package com.example.expensetracker.dtos;

import com.example.expensetracker.model.ExpenseType;
import com.example.expensetracker.model.User;
import com.googlecode.jmapper.annotations.JGlobalMap;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JGlobalMap
public class ExpenseDto {
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private Double amount;

    private User debtor;

    private List<User> creditors = new ArrayList<>();

    private Boolean isGroupExpense;

    public ExpenseDto(Long id, String description, ExpenseType type, Double amount, User debtor, List<User> creditors, Boolean isGroupExpense) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.debtor = debtor;
        this.creditors = creditors;
        this.isGroupExpense = isGroupExpense;
    }

    public ExpenseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public List<User> getCreditors() {
        return creditors;
    }

    public void setCreditors(List<User> creditors) {
        this.creditors = creditors;
    }

    public Boolean getIsGroupExpense() {
        return isGroupExpense;
    }

    public void setIsGroupExpense(Boolean groupExpense) {
        isGroupExpense = groupExpense;
    }
}
