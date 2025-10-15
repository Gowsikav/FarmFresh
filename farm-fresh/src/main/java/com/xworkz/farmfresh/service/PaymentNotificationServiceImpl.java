package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.NotificationEntity;
import com.xworkz.farmfresh.repository.AdminRepository;
import com.xworkz.farmfresh.repository.CollectMilkRepository;
import com.xworkz.farmfresh.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PaymentNotificationServiceImpl implements PaymentNotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CollectMilkRepository collectMilkRepository;

    @Autowired
    private AdminRepository adminRepository;

    public PaymentNotificationServiceImpl()
    {
        log.info("PaymentNotificationServiceImpl constructor");
    }

    @Override
    public void generateAdvanceNotifications() {
        log.info("generate advance notifications method in PaymentNotificationServiceImpl");
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        if (dayOfMonth != 13 && dayOfMonth != 28) {
            log.info("Not the 13th or 28th, skipping notification generation. Today: {}", today);
            return;
        }

        LocalDate periodStart;
        LocalDate periodEnd;
        LocalDate paymentDate;
        String dayStr;

        if (dayOfMonth == 13) {

            LocalDate prevMonth = today.minusMonths(1);
            int startDay = Math.min(30, prevMonth.lengthOfMonth());
            periodStart = prevMonth.withDayOfMonth(startDay);
            periodEnd = today.withDayOfMonth(12);
            paymentDate = today.withDayOfMonth(15);
            dayStr = "15th";
        } else {

            periodStart = today.withDayOfMonth(15);
            periodEnd = today.withDayOfMonth(27);

            int lastDayOfMonth = today.lengthOfMonth();
            int paymentDay = Math.min(30, lastDayOfMonth);
            paymentDate = today.withDayOfMonth(paymentDay);
            dayStr = paymentDay + "th";
        }

        log.info("Checking collections from {} to {} for payment on {}", periodStart, periodEnd, paymentDate);

        int supplierCount = collectMilkRepository.countSuppliersWithCollections(periodStart, periodEnd);
        if (supplierCount <= 0) {
            log.info("No suppliers found for period {} to {}", periodStart, periodEnd);
            return;
        }

        // Fan-out one notification per admin, avoid duplicates per admin per payment date
        List<AdminEntity> admins = adminRepository.findAll();
        for (AdminEntity admin : admins) {
            if (notificationRepository.existsAdvanceForPaymentDateByAdmin(admin.getAdminId(), paymentDate)) {
                log.info("Notification already exists for admin {} on {}", admin.getAdminId(), paymentDate);
                continue;
            }

            NotificationEntity n = new NotificationEntity();
            n.setAdmin(admin);
            n.setNotificationType("ADVANCE");
            n.setIsRead(false);
            n.setPaymentDate(paymentDate);
            n.setMessage(supplierCount + " suppliers need to be paid on " + dayStr);

            if(notificationRepository.save(n))
            log.info("Created notification for admin {} for payment date {}", admin.getAdminId(), paymentDate);
            else log.error("notification not created");
        }
    }

    @Override
    public List<NotificationEntity> getNotificationsByAdminEmail(String email) {
        log.info("getNotificationsByAdminEmail method in payment notification service");
        AdminEntity admin = adminRepository.getDetailsByEmail(email);
        if (admin == null) {
            return new ArrayList<>();
        }
        return notificationRepository.findByAdminOrderByCreatedAtDesc(admin.getAdminId());
    }

    @Override
    public boolean markAsRead(Long notificationId) {
        log.info("markAsRead method in payment notification service");
        return notificationRepository.markAsRead(notificationId);
    }
}
