package com.example.expensetracker.service;

import com.example.expensetracker.dtos.CreateTripRequest;
import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.dtos.PushNotificationRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TripService {

    private final TripRepository tripRepository;
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private NotificationService notificationService;
    private PushNotificationService pushNotificationService;

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

    public void SaveTrip(CreateTripRequest tripRequest)
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
            notificationService.CreateNotificationForTrip(us, trip);
        });
    }

    public TripDto UpdateTrip(TripDto tripDto) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);
        var trip = tripRepository.findTripById(tripDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripDto.getId()));
        if(!Helpers.IsNullOrEmpty(tripDto.getAvatarUri()) && tripDto.getAvatarUri() != trip.getAvatarUri()) trip.setAvatarUri(tripDto.getAvatarUri());
        if(!Helpers.IsNullOrEmpty(tripDto.getDescription()) &&tripDto.getDescription() != trip.getDescription()) trip.setDescription(tripDto.getDescription());
        if(!Helpers.IsNullOrEmpty(tripDto.getLocation()) &&tripDto.getLocation() != trip.getLocation()) trip.setLocation(tripDto.getLocation());
        if(!Helpers.IsNullOrEmpty(tripDto.getName()) &&tripDto.getName() != trip.getName()) trip.setName(tripDto.getName());

        tripRepository.save(trip);
        return tripMapper.getDestination(trip);
    }

    public TripDto AddMember(Long tripId, User user) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        trip.addUser(userRepository.findUserById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", user.getId())));
        var tripUpdated = tripRepository.save(trip);

        notificationService.CreateNotificationForTrip(user, tripUpdated);

        JMapper<TripDto, Trip> tripMapperToDto= new JMapper<>(
                TripDto.class, Trip.class);
        var x= tripMapperToDto.getDestination(tripUpdated);
        return x;
    }

    public TripDto DeleteMember(Long tripId, User userDto) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var user = userRepository.findUserById(userDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userDto.getId()));
        trip.removeUser(user);
        var tripUpdated = tripRepository.save(trip);

        var expenses = new ArrayList<Expense>(trip.getExpenses().stream().filter(e -> e.getCreditors().contains(user) || e.getDebtor() == user).collect(Collectors.toList()));
        expenses.forEach(e -> {
            if(e.getDebtor() == user){
                var creditors = new ArrayList<>(e.getCreditors());
                creditors.forEach(us -> e.removeCreditor(userRepository.findUserById(us.getId()).get()));
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

    public TripDto AddExpense(Long tripId, ExpenseDto expenseDto) {
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
        creditors.forEach(us -> expense.addCreditor(userRepository.findUserById(us.getId()).get()));

        expenseRepository.save(expense);
        if(expense.getIsGroupExpense()) notificationService.CreateNotificationsForGroupExpense(expense);

        trip.getExpenses().add(expense);
        return tripMapper.getDestination(trip);
    }

    public TripDto DeleteExpense(Long tripId, ExpenseDto expenseDto) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);

        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var expense = expenseRepository.findExpenseById(expenseDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Expense", "expenseId", expenseDto.getId()));
        expense.setTrip(trip);
        RemoveCreditors(expense);
        trip.getExpenses().remove(expense);
        expenseRepository.delete(expense);
        return tripMapper.getDestination(trip);
    }

    public void DeleteTrip(Long tripId){
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        var members = trip.getUsers();
        var expenses = trip.getExpenses();
        members.forEach(m -> m.getTrips().remove(trip));
        expenses.forEach(e -> {
            RemoveCreditors(e);
            expenseRepository.delete(e);
        });
        notificationService.DeleteNotificationsForTrip(tripId);
        tripRepository.delete(trip);
    }

    private void RemoveCreditors(Expense expense)
    {
        var creditors = new ArrayList<>(expense.getCreditors());
        creditors.forEach(us -> expense.removeCreditor(userRepository.findUserById(us.getId()).get()));
    }

}
