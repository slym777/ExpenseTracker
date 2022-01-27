package com.example.expensetracker.service;

import com.example.expensetracker.dtos.CreateTripRequest;
import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.dtos.TripDto;
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
        var trip = tripRepository.findTripById(tripId);
        return tripMapper.getDestination(trip.get());
    }

    public List<TripDto> getTripsByUserId(Long userId) {
        JMapper<TripDto, Trip> tripMapper= new JMapper<>(
                TripDto.class, Trip.class);
        var user = userRepository.findUserById(userId).get();
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
        users.forEach(us -> trip.addUser(userRepository.findUserById(us.getId()).get()));
        tripRepository.save(trip);
    }

    public TripDto AddMember(Long tripId, User user) {
        var trip = tripRepository.getById(tripId);
        trip.setGroupSize(trip.getGroupSize() + 1);
        trip.addUser(userRepository.findUserById(user.getId()).get());
        var tripUpdated = tripRepository.save(trip);
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

        var trip = tripRepository.getById(tripId);
        var expense = expenseMapper.getDestination(expenseDto);
        expense.setTrip(trip);
        expense.setCreditors(new ArrayList<>());
        var creditors = new ArrayList<>(expenseDto.getCreditors());
        creditors.forEach(us -> expense.addCreditor(userRepository.findUserById(us.getId()).get()));
        expenseRepository.save(expense);
        trip.getExpenses().add(expense);
        return tripMapper.getDestination(trip);
    }
}
