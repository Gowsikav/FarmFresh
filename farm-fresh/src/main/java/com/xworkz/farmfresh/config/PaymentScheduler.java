package com.xworkz.farmfresh.config;

import com.xworkz.farmfresh.service.PaymentNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class PaymentScheduler {

    @Autowired
    private PaymentNotificationService paymentNotificationService;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Kolkata") // runs daily at 9 AM
    public void runAdvanceNotification() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int lastDayOfMonth = today.lengthOfMonth();

        if (dayOfMonth == 13 || dayOfMonth == lastDayOfMonth - 2) {
            log.info("Triggering advance payment notification for {}", today);
            paymentNotificationService.generateAdvanceNotifications();
        } else {
            log.info("Today ({}) is not 13th or {} (2 days before month end). Skipping advance notification.",
                    dayOfMonth, lastDayOfMonth - 2);
        }
    }


    // every day at 9 AM    // 0 */1 * * * *
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Kolkata")
    public void runPaymentNotification() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int lastDayOfMonth = today.lengthOfMonth();

        if (dayOfMonth == 15 || dayOfMonth == lastDayOfMonth) {
            paymentNotificationService.generatePaymentNotifications();
        } else {
            log.info("Today ({}) is not 15th or last day ({}), skipping payment notification.",
                    dayOfMonth, lastDayOfMonth);
        }
    }

    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Kolkata") // every day at 6 PM
    public void runEveningPaymentSummary() {
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int lastDayOfMonth = today.lengthOfMonth();

        if (dayOfMonth == 15 || dayOfMonth == lastDayOfMonth) {
            if(paymentNotificationService.getPaymentDetailsForAdminEmailSummary())
                log.info("Email summary send to admins");
        } else {
            log.info("Today ({}) is not 15th or last day ({}), skipping admin payment summary.",
                    dayOfMonth, lastDayOfMonth);
        }
    }

}
