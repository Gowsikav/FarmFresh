package com.xworkz.farmfresh.config;

import com.xworkz.farmfresh.service.PaymentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    @Autowired
    private PaymentNotificationService paymentNotificationService;

    //   0 0 9 13,28 * *   every month 13 and 28 at 9 AM
    @Scheduled(cron = "0 0 9 13,28 * *",zone = "Asia/Kolkata")
    public void runAdvanceNotification() {
        paymentNotificationService.generateAdvanceNotifications();
    }

    // 0 0 9 15,30 * *  every month 15 and 30 at 9 AM     // 0 */1 * * * *
    @Scheduled(cron = "0 0 9 15,30 * *", zone = "Asia/Kolkata")
    public void runPaymentNotification() {
        paymentNotificationService.generatePaymentNotifications();
    }

}
