package com.example.expensetracker.repository;

import com.example.expensetracker.model.Notification;
import com.example.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUser(User user);
    Optional<Notification> findById(Long id);
}
