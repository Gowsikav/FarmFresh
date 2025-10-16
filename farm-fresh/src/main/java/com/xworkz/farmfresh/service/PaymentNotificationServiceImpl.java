package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.NotificationEntity;
import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;
import com.xworkz.farmfresh.repository.*;
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

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    public PaymentNotificationServiceImpl() {
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

            if (notificationRepository.save(n))
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

    @Override
    public void generatePaymentNotifications() {
        log.info("generate payment notifications method in PaymentNotificationServiceImpl");
        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();

        if (dayOfMonth != 15 && dayOfMonth != 30) {
            log.info("Not the 15th or 30th, skipping payment notification generation");
            return;
        }

        LocalDate periodStart;
        LocalDate periodEnd;

        if (dayOfMonth == 15) {
            periodStart = today.minusMonths(1).withDayOfMonth(30);
            periodEnd = today.withDayOfMonth(14);
        } else {
            periodStart = today.withDayOfMonth(15);
            periodEnd = today.withDayOfMonth(29);
        }

        List<Object[]> supplierList = collectMilkRepository.getEntityForPaymentNotification(periodStart, periodEnd);
        List<AdminEntity> admins = adminRepository.findAll();
        log.error("size {} {}",supplierList.size(),admins.size());

        for (Object[] row : supplierList) {
            SupplierEntity supplier = (SupplierEntity) row[0];
            Double totalAmount = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            log.error("supplier {} , total amount {}",supplier,totalAmount);

            PaymentDetailsEntity paymentDetailsEntity = new PaymentDetailsEntity();
            paymentDetailsEntity.setSupplier(supplier);
            paymentDetailsEntity.setAdmin(null);
            paymentDetailsEntity.setPeriodStart(periodStart);
            paymentDetailsEntity.setPeriodEnd(periodEnd);
            paymentDetailsEntity.setTotalAmount(totalAmount);
            paymentDetailsEntity.setPaymentDate(today);

            if (paymentDetailsRepository.save(paymentDetailsEntity))
                log.info("payment details saved for " + supplier.getSupplierId());
            else log.error("payment details not saved for " + supplier.getSupplierId());

            for (AdminEntity admin : admins) {
                NotificationEntity n = new NotificationEntity();
                n.setAdmin(admin);
                n.setSupplier(supplier);
                n.setNotificationType("PAYMENT");
                n.setIsRead(false);
                n.setMessage("Pay Rs." + totalAmount + " to " + supplier.getFirstName() + " " + supplier.getLastName() + " (" + periodStart + " to " + periodEnd + ")");
                n.setPaymentDate(today);
                n.setAmount(totalAmount);

                if (notificationRepository.save(n))
                    log.info("Payment notification created for admin {}", admin.getAdminId());
                 else log.error("Failed to create payment notification for admin {}", admin.getAdminId());
            }
        }

    }

    @Override
    public Double getAmountById(Long notificationId) {
        log.info("getAmountById method in notification service");
        NotificationEntity notification=notificationRepository.getNotificationById(notificationId);
        return notification!=null?notification.getAmount():0.0;
    }

    @Override
    public boolean markAsReadForPayment(Long notificationId, String supplierEmail,String adminEmail) {
        log.info("markAsReadForPayment method in notification service");
        NotificationEntity notification=notificationRepository.getNotificationById(notificationId);
        if(notification==null)
        {
            log.error("notification not found");
            return false;
        }

        AdminEntity adminEntity=adminRepository.getDetailsByEmail(adminEmail);
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(supplierEmail);
        if(adminEntity==null || supplierEntity==null)
            return false;
        PaymentDetailsEntity paymentDetailsEntity=paymentDetailsRepository.
                getEntityBySupplierIdAndPaymentDate(notification.getPaymentDate(),supplierEntity.getSupplierId());
        if(paymentDetailsEntity==null)
        {
            log.error("payment details not found");
            return false;
        }
        paymentDetailsEntity.setPaymentStatus("PAID");
        paymentDetailsEntity.setPaymentDate(LocalDate.now());
        paymentDetailsEntity.setAdmin(adminEntity);
        if(paymentDetailsRepository.update(paymentDetailsEntity))
        {
            log.info("payment details updated");
            if(notificationRepository.markAsReadForPayment(notification.getPaymentDate(),supplierEntity.getSupplierId()))
            {
                log.info("Notification updated");
                return true;
            }
        }else {
            log.error("payment details not updated");
        }

        return false;
    }
}
