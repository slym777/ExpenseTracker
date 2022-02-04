package com.example.expensetracker.repository;

import com.example.expensetracker.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAll();
    Optional<Trip> findTripById(Long id);

    @Override
    @Transactional
    Trip save(Trip trip);
}
