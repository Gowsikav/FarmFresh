package com.xworkz.farmfresh.util;

import com.xworkz.farmfresh.entity.NotificationEntity;
import com.xworkz.farmfresh.service.PaymentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class CommonControllerHelper {

    @Autowired
    private PaymentNotificationService notificationService;

    public void addNotificationData(Model model, String email) {
        List<NotificationEntity> notifications = notificationService.getNotificationsByAdminEmail(email);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", notifications.size());
    }
}
