package com.example.expensetracker.service;

import com.example.expensetracker.dtos.CreateTripRequest;
import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.dtos.TripDto;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.Trip;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.ExpenseRepository;
import com.example.expensetracker.repository.TripRepository;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.utils.Helpers;
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TripService {

    private final TripRepository tripRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PushNotificationService pushNotificationService;

    @Autowired
    public TripService(TripRepository tripRepository, ExpenseRepository expenseRepository, UserRepository userRepository,
                       NotificationService notificationService, PushNotificationService pushNotificationService) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.pushNotificationService = pushNotificationService;
    }

    public TripDto getTripById(Long tripId) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        return tripMapper.getDestination(trip);
    }

    public List<TripDto> getTripsByUserId(Long userId) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);
        var user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        var trips = user.getTrips();
        var tripsDto = new ArrayList<TripDto>();
        trips.forEach(t -> tripsDto.add(tripMapper.getDestination(t)));
        return tripsDto;
    }

    public List<TripDto> getAllTrips(){
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);
        var trips = tripRepository.findAll();
        var tripsDto = new ArrayList<TripDto>();
        trips.forEach(t -> tripsDto.add(tripMapper.getDestination(t)));
        return tripsDto;
    }

    public void saveTrip(CreateTripRequest tripRequest)
    {
        JMapper<Trip, CreateTripRequest> tripMapper= new JMapper<>(
                Trip.class, CreateTripRequest.class);
        var trip = tripMapper.getDestination(tripRequest);
        trip.setUsers(new ArrayList<>());
        trip.setGroupSize(0);
        var users = new ArrayList<>(tripRequest.getUsers());
        users.forEach(us -> {
            trip.addUser(userRepository.findUserById(us.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("User", "userId", us.getId())));
        });

        tripRepository.save(trip);

        users.forEach(us -> {
            pushNotificationService.createAddUserToTripNotification(us.getId(), trip.getName());
            notificationService.createNotificationForTrip(us, trip);
        });
    }

    public TripDto updateTrip(Long tripId, TripDto tripDto) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        if(!Helpers.IsNullOrEmpty(tripDto.getAvatarUri()) && tripDto.getAvatarUri() != trip.getAvatarUri()) trip.setAvatarUri(tripDto.getAvatarUri());
        if(!Helpers.IsNullOrEmpty(tripDto.getDescription()) &&tripDto.getDescription() != trip.getDescription()) trip.setDescription(tripDto.getDescription());
        if(!Helpers.IsNullOrEmpty(tripDto.getLocation()) &&tripDto.getLocation() != trip.getLocation()) trip.setLocation(tripDto.getLocation());
        if(!Helpers.IsNullOrEmpty(tripDto.getName()) &&tripDto.getName() != trip.getName()) trip.setName(tripDto.getName());

        tripRepository.save(trip);

        var savedUsers = trip.getUsers();
        var incomingUsers = tripDto.getUsers();

        List<User> toBeDeleted = new ArrayList<>();
        for(var savedUser: savedUsers) {
            boolean isPresent = false;
            for (var incomingUser : incomingUsers) {
                if (savedUser.getEmail().equals(incomingUser.getEmail())) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                toBeDeleted.add(savedUser);
            }
        }

        toBeDeleted.forEach(user -> deleteMember(tripId, user));

        List<User> toBeAdded = new ArrayList<>();
        for(var incomingUser: incomingUsers) {
            boolean isPresent = false;
            for (var savedUser : savedUsers) {
                if (savedUser.getEmail().equals(incomingUser.getEmail())) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                toBeAdded.add(incomingUser);
            }
        }

        toBeAdded.forEach(user -> addMember(tripId, user));

        return getTripById(tripId);
    }

    public TripDto addMember(Long tripId, User user) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        trip.addUser(userRepository.findUserById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", user.getId())));
        var tripUpdated = tripRepository.save(trip);

        notificationService.createNotificationForTrip(user, tripUpdated);
        pushNotificationService.createAddUserToTripNotification(user.getId(), tripUpdated.getName());

        JMapper<TripDto, Trip> tripMapperToDto= new JMapper<>(
                TripDto.class, Trip.class);
        var x= tripMapperToDto.getDestination(tripUpdated);
        return x;
    }

    public TripDto deleteMember(Long tripId, User userDto) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var user = userRepository.findUserById(userDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userDto.getId()));
        trip.removeUser(user);
        var tripUpdated = tripRepository.save(trip);

        var expenses = new ArrayList<>(trip.getExpenses().stream().filter(e -> e.getCreditors().contains(user) || e.getDebtor() == user).collect(Collectors.toList()));
        expenses.forEach(e -> {
            if(e.getDebtor() == user){
                var creditors = new ArrayList<>(e.getCreditors());
                creditors.forEach(us -> e.removeCreditor(userRepository.findUserById(us.getId()).orElseThrow(
                        () -> new ResourceNotFoundException("User", "userId", us.getId())
                )));
                trip.getExpenses().remove(e);
                expenseRepository.delete(e);
            }
            else {
                e.removeCreditor(user);
                expenseRepository.save(e);
            }
        });

        JMapper<TripDto, Trip> tripMapperToDto= new JMapper<>(
                TripDto.class, Trip.class);
        var x= tripMapperToDto.getDestination(tripUpdated);
        return x;
    }

    public TripDto addExpense(Long tripId, ExpenseDto expenseDto) {
        JMapper<Expense, ExpenseDto> expenseMapper= new JMapper<>(
                Expense.class, ExpenseDto.class);
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);

        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var expense = expenseMapper.getDestination(expenseDto);
        expense.setTrip(trip);
        expense.setDebtor(userRepository.findUserById(expenseDto.getDebtor().getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", expenseDto.getDebtor().getId())));
        expense.setCreditors(new ArrayList<>());
        var creditors = new ArrayList<>(expenseDto.getCreditors());
        creditors.forEach(us -> expense.addCreditor(userRepository.findUserById(us.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", us.getId()))));

        expense.setCreatedDate(Date.from(Instant.now()));
        expenseRepository.save(expense);

        if(expense.getIsGroupExpense()) {
            notificationService.createNotificationsForGroupExpense(expense);

            expense.getCreditors().forEach(user -> {
                if (!user.equals(expense.getDebtor())) {
                    pushNotificationService.createAddExpenseForUserNotification(user.getId(), expense);
                }
            });
        }

        trip.getExpenses().add(expense);
        return tripMapper.getDestination(trip);
    }

    public TripDto deleteExpense(Long tripId, ExpenseDto expenseDto) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);

        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var expense = expenseRepository.findExpenseById(expenseDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expenseId", expenseDto.getId()));
        expense.setTrip(trip);
        removeCreditors(expense);
        trip.getExpenses().remove(expense);
        expenseRepository.delete(expense);
        return tripMapper.getDestination(trip);
    }

    public void deleteTrip(Long tripId){
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var members = trip.getUsers();
        var expenses = trip.getExpenses();
        members.forEach(m -> m.getTrips().remove(trip));
        expenses.forEach(e -> {
            removeCreditors(e);
            expenseRepository.delete(e);
        });
        notificationService.deleteNotificationsForTrip(tripId);
        tripRepository.delete(trip);
    }

    private void removeCreditors(Expense expense)
    {
        var creditors = new ArrayList<>(expense.getCreditors());
        creditors.forEach(us -> expense.removeCreditor(userRepository.findUserById(us.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", us.getId())
        )));
    }
}
