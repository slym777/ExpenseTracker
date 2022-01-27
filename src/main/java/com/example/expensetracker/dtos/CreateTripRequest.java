package com.example.expensetracker.dtos;

import com.example.expensetracker.model.User;
import com.googlecode.jmapper.annotations.JGlobalMap;

import java.util.ArrayList;
import java.util.List;

@JGlobalMap
public class CreateTripRequest {
    private String name;

    private String description;

    private String avatarUri;

    private String location;


    private List<User> users = new ArrayList<>();

    public CreateTripRequest(String name, String description, String avatarUri, String location, List<User> users) {
        this.name = name;
        this.description = description;
        this.avatarUri = avatarUri;
        this.location = location;
        this.users = users;
    }

    public CreateTripRequest() {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
