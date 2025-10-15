package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.NotificationEntity;

import java.util.List;

public interface PaymentNotificationService {
    void generateAdvanceNotifications();
    List<NotificationEntity> getNotificationsByAdminEmail(String email);
    void markAsRead(Long notificationId);

}
