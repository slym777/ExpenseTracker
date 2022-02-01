package com.example.expensetracker.service;

import com.example.expensetracker.dtos.PushNotificationRequest;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.PushNotification;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.PushNotificationRepository;
import com.example.expensetracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.apache.http.util.TextUtils.isBlank;

@Service
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private final FCMService fcmService;

    private final PushNotificationRepository notificationRepository;

    private final UserRepository userRepository;


    @Autowired
    public PushNotificationService(FCMService fcmService, PushNotificationRepository notificationRepository,
                                   UserRepository userRepository) {
        this.fcmService = fcmService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void updateNotificationToken(String token, Long userId){
        Optional<PushNotification> pushNotification = notificationRepository.findByUserId(userId);
        if (pushNotification.isPresent()) {
            if (!token.equals(pushNotification.get().getNotificationToken())) {
                notificationRepository.updateNotificationToken(token, userId);
            }
        } else {
            insertNotificationToken(token, userId);
        }
    }

    public void insertNotificationToken(String token, Long userId){
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId));
        PushNotification notificationToken = new PushNotification(user, token);
        notificationRepository.save(notificationToken);
    }

    public PushNotificationRequest test(Long userId) {
        String message = "Test";
        String token = notificationRepository.getNotificationTokenByUserId(userId);
        PushNotificationRequest request = createPushNotificationRequest(null, message, token);
        this.sendPushNotificationToToken(request);
        return request;
    }

    public void createAddUserToTripNotification(Long userId, String tripName) {
        String message = "You have been added to " + tripName;
        String token = notificationRepository.getNotificationTokenByUserId(userId);
        PushNotificationRequest request = createPushNotificationRequest(null, message, token);
        this.sendPushNotificationToToken(request);
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    private PushNotificationRequest createPushNotificationRequest(String title, String message, String token) {
        return PushNotificationRequest.builder()
                .setTitle(isBlank(title) ? defaults.get("title") : title)
                .setMessage(isBlank(message) ? defaults.get("title") : message)
                .setToken(token)
                .build();
    }

    private PushNotificationRequest getDefaultPushNotificationRequest() {
        return PushNotificationRequest.builder()
                .setTitle(defaults.get("title"))
                .setMessage(defaults.get("message")).
                setTopic( defaults.get("topic")).build();
    }
}


