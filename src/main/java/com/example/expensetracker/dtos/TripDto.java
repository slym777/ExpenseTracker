package com.example.expensetracker.dtos;

import com.example.expensetracker.model.User;
import com.googlecode.jmapper.annotations.JGlobalMap;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@JGlobalMap
public class TripDto {
    private Long id;
    @NotBlank
    @Size(max = 40)
    private String name;

    private String description;

    private String avatarUri;

    private String location;

    private Integer groupSize;

    private List<User> users = new ArrayList<>();

    private List<ExpenseDto> expenses = new ArrayList<>();

    public TripDto(Long id, String name, String description, String avatarUri, String location, Integer groupSize, List<User> users, List<ExpenseDto> expenses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.avatarUri = avatarUri;
        this.location = location;
        this.groupSize = groupSize;
        this.users = users;
        this.expenses = expenses;
    }

    public TripDto() {
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

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<ExpenseDto> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDto> expenses) {
        this.expenses = expenses;
    }
}
