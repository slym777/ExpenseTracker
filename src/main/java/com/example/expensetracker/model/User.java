package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String fullName;

    @NaturalId(mutable=true)
    @NotBlank
    @Size(max = 40)
    @Email
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phoneNumber;

    private String avatarUri;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "user_trip",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "trip_id"))
    private List<Trip> trips = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "debtor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    @Where(clause = "settled_up = false")
    private List<Expense> debtorExpenses = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "user_expense",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_id"))
    private List<Expense> creditorExpenses = new ArrayList<>();

    public User() {
    }

    public User(String fullName, String email, String phoneNumber, String avatarUri) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUri = avatarUri;
    }

    public User(Long id, String fullName, String email, String phoneNumber, String avatarUri, List<Trip> trips, List<Expense> debtorExpenses, List<Expense> creditorExpenses) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUri = avatarUri;
        this.trips = trips;
        this.debtorExpenses = debtorExpenses;
        this.creditorExpenses = creditorExpenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Expense> getDebtorExpenses() {
        return debtorExpenses;
    }

    public void setDebtorExpenses(List<Expense> debtorExpenses) {
        this.debtorExpenses = debtorExpenses;
    }

    public List<Expense> getCreditorExpenses() {
        return creditorExpenses;
    }

    public void setCreditorExpenses(List<Expense> creditorExpenses) {
        this.creditorExpenses = creditorExpenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(fullName, user.fullName) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(avatarUri, user.avatarUri) && Objects.equals(trips, user.trips) && Objects.equals(debtorExpenses, user.debtorExpenses) && Objects.equals(creditorExpenses, user.creditorExpenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, phoneNumber);
    }
}
