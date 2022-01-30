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
import com.googlecode.jmapper.JMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class TripService {

    private final TripRepository tripRepository;
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;

    @Autowired
    public TripService(TripRepository tripRepository, ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
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
    }

    public TripDto AddMember(Long tripId, User user) {
        var trip = tripRepository.findTripById(tripId).orElseThrow(
                () -> new ResourceNotFoundException("Trip", "tripId", tripId));
        trip.addUser(userRepository.findUserById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", user.getId())));
        var tripUpdated = tripRepository.save(trip);
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
        expense.setCreditors(new ArrayList<>());
        var creditors = new ArrayList<>(expenseDto.getCreditors());
        creditors.forEach(us -> expense.addCreditor(userRepository.findUserById(us.getId()).get()));
        expenseRepository.save(expense);
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
        var creditors = new ArrayList<>(expense.getCreditors());
        creditors.forEach(us -> expense.removeCreditor(userRepository.findUserById(us.getId()).get()));
        trip.getExpenses().remove(expense);
        expenseRepository.delete(expense);
        return tripMapper.getDestination(trip);
    }
}
