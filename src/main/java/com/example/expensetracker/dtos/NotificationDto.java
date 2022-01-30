package com.example.expensetracker.dtos;

import com.example.expensetracker.model.ActionType;
import com.example.expensetracker.model.User;
import com.googlecode.jmapper.annotations.JGlobalMap;

@JGlobalMap
public class NotificationDto {
    private Long id;

    private String description;

    private ActionType type;

    private User user;

    public NotificationDto(Long id, String description, User user) {
        this.id = id;
        this.description = description;
        this.user = user;
    }

    public NotificationDto() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }
}
