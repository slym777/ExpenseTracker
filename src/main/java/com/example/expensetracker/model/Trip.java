package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    private String description;

    private String avatarUri;

    private String location;

    private Integer groupSize;

    //@JsonIgnore
    @ManyToMany(mappedBy = "trips", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<User> users = new ArrayList<>();

    //@JsonIgnore
    @OneToMany(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Expense> expenses = new ArrayList<>();

    public Trip() {
    }

    public Trip(Long id, String name, String description, String avatarUri, String location, Integer groupSize, List<User> users, List<Expense> expenses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.avatarUri = avatarUri;
        this.location = location;
        this.groupSize = groupSize;
        this.users = users;
        this.expenses = expenses;
    }

    public void addUser(User user){
        users.add(user);
        user.getTrips().add(this);
        groupSize++;
    }

    public void removeUser(User user){
        users.remove(user);
        user.getTrips().remove(this);
        groupSize--;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) && Objects.equals(name, trip.name) && Objects.equals(description, trip.description) && Objects.equals(avatarUri, trip.avatarUri) && Objects.equals(location, trip.location) && Objects.equals(groupSize, trip.groupSize) && Objects.equals(users, trip.users) && Objects.equals(expenses, trip.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}
