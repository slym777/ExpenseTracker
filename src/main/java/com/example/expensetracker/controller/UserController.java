package com.example.expensetracker.controller;

import com.example.expensetracker.dtos.UserDto;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.UserRepository;
import com.example.expensetracker.service.TripService;
import com.example.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @GetMapping("/userEmail={userEmail}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok().body(userService.getUserByEmail(userEmail));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/saveUser")
    public ResponseEntity<UserDto> saveTrip(@RequestBody UserDto userDto) {
        var savedUserDto = userService.saveUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

// public ResponseEntity<?> getTripById(@PathVariable Long tripId){ return ResponseEntity.ok().body(tripService.getTripById(tripId)); }
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/changePassword")
//    public ResponseEntity updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @CurrentUser UserPrincipal currentUser) {
//        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentUser.getPassword())){
//            return new ResponseEntity(new ApiResponse(false, "Incorrect Password"),
//                    HttpStatus.BAD_REQUEST);
//        }
//
//        User user = userRepository.findUserById(currentUser.getId()).orElseThrow(
//                () -> new ResourceNotFoundException("User", "userId", currentUser.getId()));
//        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
//        userRepository.save(user);
//
//        return new ResponseEntity(new ApiResponse(true, "Password updated successful"), HttpStatus.OK);
//    }
}
