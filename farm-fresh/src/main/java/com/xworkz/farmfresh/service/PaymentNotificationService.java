package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.NotificationEntity;

import java.util.List;

public interface PaymentNotificationService {
    void generateAdvanceNotifications();
    List<NotificationEntity> getNotificationsByAdminEmail(String email);
    boolean markAsRead(Long notificationId);
    void generatePaymentNotifications();
    Double getAmountById(Long notificationId);
    boolean markAsReadForPayment(Long notificationId,String supplierEmail,String adminEmail);
    Double getTotalAmountPaid(Integer supplierId);
}
