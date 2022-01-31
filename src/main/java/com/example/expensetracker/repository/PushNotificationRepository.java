package com.example.expensetracker.repository;

import com.example.expensetracker.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {

    @Query(value = "update push_notification n set n.notification_token=:token where n.user_id=:userId ",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateNotificationToken(String token, Long userId);

    @Query(value = "select n.notification_token from push_notification n where n.user_id=:userId",
            nativeQuery = true)
    String getNotificationTokenByUserId(Long userId);


    Optional<PushNotification> findByUserId(Long userId);

    Optional<PushNotification> findByNotificationToken(String notificationToken);
}