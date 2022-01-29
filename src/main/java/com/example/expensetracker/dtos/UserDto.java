package com.example.expensetracker.dtos;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.Trip;
import com.example.expensetracker.model.User;
import com.googlecode.jmapper.annotations.JGlobalMap;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@JGlobalMap
public class UserDto {
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String fullName;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phoneNumber;

    private String avatarUri;

    private List<Trip> trips = new ArrayList<>();

    private List<Expense> debtorExpenses = new ArrayList<>();

    private List<Expense> creditorExpenses = new ArrayList<>();

    public UserDto() { }

    public UserDto(String fullName, String email, String phoneNumber, String avatarUri) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatarUri = avatarUri;
    }

    public UserDto(Long id, String fullName, String email, String phoneNumber, String avatarUri, List<Trip> trips, List<Expense> debtorExpenses, List<Expense> creditorExpenses) {
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
}
