package com.example.expensetracker.service;

import com.example.expensetracker.dtos.NotificationDto;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.*;
import com.example.expensetracker.repository.NotificationRepository;
import com.example.expensetracker.repository.UserRepository;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository)
    {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void CreateNotificationsForGroupExpense(Expense expense)
    {
        var sizeGroup = expense.getCreditors().size();
        var messageTemplate = String.format("You have to pay %s to %s", expense.getAmount()/sizeGroup, expense.getDebtor().getFullName());
        expense.getCreditors().stream().filter(c -> c != expense.getDebtor())
                .forEach(c -> notificationRepository.save(new Notification(messageTemplate, ActionType.CREDITOR_IN_EXPENSE, c, expense.getTrip())));
    }

    public void CreateNotificationForTrip(User user, Trip trip)
    {
        var messageTemplate = String.format("You have been added to trip: %s", trip.getName());
        notificationRepository.save(new Notification(messageTemplate, ActionType.ADDED_TO_TRIP, user, trip));
    }

    public List<NotificationDto> GetNotificationsForUser(Long userId)
    {
        JMapper<NotificationDto, Notification> notificationMapper= new JMapper<>(
                NotificationDto.class, Notification.class);
        var user = userRepository.findUserById(userId).orElseThrow(
            () -> new ResourceNotFoundException("User", "userId", userId));
        return notificationRepository.findAllByUser(user).stream().map(notificationMapper::getDestination).collect(Collectors.toList());
    }
    public void DeleteNotification(Long notificationId)
    {
        var notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new ResourceNotFoundException("Notification", "notificationId", notificationId));
        notificationRepository.delete(notification);
    }

    public void DeleteNotificationsForTrip(Long tripId)
    {
        var notifications = notificationRepository.findAll().stream().filter(n ->  n.getTrip().getId() == tripId).collect(Collectors.toList());
        notificationRepository.deleteAll(notifications);
    }
}
