package com.example.expensetracker.controller;

import com.example.expensetracker.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pushNotification")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;

    @Autowired
    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/updateToken/{token}/{userId}")
    public ResponseEntity<?> updateNotificationToken(@PathVariable String token, @PathVariable Long userId){
        pushNotificationService.updateNotificationToken(token, userId);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/test/{userId}")
    public ResponseEntity<?> test(@PathVariable Long userId){
        return ResponseEntity.ok().body(pushNotificationService.test(userId));
    }


}