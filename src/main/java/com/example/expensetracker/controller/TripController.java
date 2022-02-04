package com.example.expensetracker.controller;

import com.example.expensetracker.dtos.CreateTripRequest;
import com.example.expensetracker.dtos.ExpenseDto;
import com.example.expensetracker.dtos.TripDto;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/allTrips")
    public ResponseEntity<?> getAllTrips(){ return ResponseEntity.ok().body(tripService.getAllTrips()); }

    @GetMapping("/{tripId}")
    public ResponseEntity<?> getTripById(@PathVariable Long tripId){ return ResponseEntity.ok().body(tripService.getTripById(tripId)); }

    @GetMapping("/userId={userId}")
    public ResponseEntity<?> getTripsByUserId(@PathVariable Long userId){ return ResponseEntity.ok().body(tripService.getTripsByUserId(userId)); }

    @PostMapping("/saveTrip")
    public ResponseEntity<?> saveTrip(@RequestBody CreateTripRequest trip) {
        tripService.saveTrip(trip);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteTrip/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updateTrip/{tripId}")
    public ResponseEntity<?> updateTrip(@PathVariable Long tripId, @RequestBody TripDto tripDto) {return ResponseEntity.ok().body(tripService.updateTrip(tripId, tripDto));}

    @PostMapping("/addMember/{tripId}")
    public ResponseEntity<?> addMemberToTrip(@PathVariable Long tripId, @RequestBody User user) {return ResponseEntity.ok().body(tripService.addMember(tripId, user));}

    @DeleteMapping("/deleteMember/{tripId}")
    public ResponseEntity<?> deleteMemberFromTrip(@PathVariable Long tripId, @RequestBody User user) {return ResponseEntity.ok().body(tripService.deleteMember(tripId, user));}

    @PostMapping("/addExpense/{tripId}")
    public ResponseEntity<?> addExpenseToTrip(@PathVariable Long tripId, @RequestBody ExpenseDto expenseDto) {return ResponseEntity.ok().body(tripService.addExpense(tripId, expenseDto));}

    @DeleteMapping("/deleteExpense/{tripId}")
    public ResponseEntity<?> deleteExpenseFromTrip(@PathVariable Long tripId, @RequestBody ExpenseDto expenseDto) {return ResponseEntity.ok().body(tripService.deleteExpense(tripId, expenseDto));}
}
