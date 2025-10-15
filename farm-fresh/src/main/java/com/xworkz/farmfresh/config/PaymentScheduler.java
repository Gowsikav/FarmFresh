package com.xworkz.farmfresh.config;

import com.xworkz.farmfresh.service.PaymentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    @Autowired
    private PaymentNotificationService paymentNotificationService;

    // Runs at 09:00 on the 13th of every month  0 0 9 13 * ?    0 0 9 13,28 * *
    @Scheduled(cron = "0 */1 * * * *",zone = "Asia/Kolkata")
    public void runAdvanceNotification() {
        paymentNotificationService.generateAdvanceNotifications();
    }
}
