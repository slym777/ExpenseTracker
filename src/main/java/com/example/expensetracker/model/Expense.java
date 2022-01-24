package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private Double amount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
//    @Where(clause = "settleUp = false")
    private User debtor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
//    @Where(clause = "settleUp = false")
    private User creditor;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

//    @Column(columnDefinition = "boolean default false")
//    private Boolean settledUp;


    public Expense() {
    }

    public Expense(Long id, String description, ExpenseType type, Double amount, User debtor, User creditor, Trip trip) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.debtor = debtor;
        this.creditor = creditor;
        this.trip = trip;
    }

    public void addDebtor(User debtor){
        this.debtor = debtor;
    }

    public void addCreditor(User creditor){
        this.creditor = creditor;
    }

    public void addTrip(Trip trip){
        this.trip = trip;
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

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id.equals(expense.id) && Objects.equals(description, expense.description) && type == expense.type && amount.equals(expense.amount) && Objects.equals(debtor, expense.debtor) && Objects.equals(creditor, expense.creditor) && Objects.equals(trip, expense.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, type, amount, debtor, creditor, trip);
    }
}
