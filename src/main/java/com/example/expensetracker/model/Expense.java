package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "debtor_id", nullable = false)
    private User debtor;

    @ManyToMany(mappedBy = "creditorExpenses", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> creditors = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(columnDefinition = "boolean default false")
    private Boolean isGroupExpense;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdDate;

    public Expense() {
    }

    public Expense(Long id, String description, ExpenseType type, Double amount, User debtor, ArrayList<User> creditors, Trip trip, Boolean isGroupExpense) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.debtor = debtor;
        this.creditors = creditors;
        this.trip = trip;
        this.isGroupExpense = isGroupExpense;
        this.createdDate = Date.from(Instant.now());
    }

    public void addDebtor(User debtor){
        this.debtor = debtor;
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

    public void addCreditor(User user){
        creditors.add(user);
        user.getCreditorExpenses().add(this);
    }

    public void removeCreditor(User user){
        creditors.remove(user);
        user.getCreditorExpenses().remove(this);
    }

    public List<User> getCreditors() {
        return creditors;
    }

    public void setCreditors(List<User> creditors) {
        this.creditors = creditors;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Boolean getIsGroupExpense() { return isGroupExpense;}

    public void setIsGroupExpense(Boolean groupExpense) { isGroupExpense = groupExpense;}

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id.equals(expense.id) && Objects.equals(description, expense.description) && type == expense.type && amount.equals(expense.amount) && Objects.equals(debtor, expense.debtor) && Objects.equals(creditors, expense.creditors) && Objects.equals(trip, expense.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, type, amount, debtor, creditors, trip);
    }
}
