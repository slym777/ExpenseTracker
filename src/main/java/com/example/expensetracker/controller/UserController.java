package com.example.expensetracker.controller;

import com.example.expensetracker.dtos.UserDto;
import com.example.expensetracker.service.NotificationService;
import com.example.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public UserController(UserService userService, NotificationService notificationService) { this.userService = userService;
        this.notificationService = notificationService;
    }

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

    @GetMapping("/{userId}/getNotifications")
    public ResponseEntity<?> getNotificationForUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(notificationService.getNotificationsForUser(userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/saveUser")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        var savedUserDto = userService.saveUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/edit/userId={userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        var updatedUserDto = userService.updateUser(userId, userDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }
}
