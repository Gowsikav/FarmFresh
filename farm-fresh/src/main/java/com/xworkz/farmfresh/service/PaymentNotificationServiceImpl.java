package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.NotificationEntity;
import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;
import com.xworkz.farmfresh.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private EmailSender emailSender;

    public PaymentNotificationServiceImpl() {
        log.info("PaymentNotificationServiceImpl constructor");
    }

    @Override
    public void generateAdvanceNotifications() {
        log.info("generate advance notifications method in PaymentNotificationServiceImpl");

        LocalDate today = LocalDate.now();
        int dayOfMonth = today.getDayOfMonth();
        int lastDayOfMonth = today.lengthOfMonth();

        if (dayOfMonth != 13 && dayOfMonth != lastDayOfMonth - 2) {
            log.info("Not the 13th or {} (two days before month end), skipping notification generation. Today: {}", lastDayOfMonth - 2, today);
            return;
        }

        LocalDate periodStart;
        LocalDate periodEnd;
        LocalDate paymentDate;
        String dayStr;

        if (dayOfMonth == 13) {
            periodStart = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());
            periodEnd = today.withDayOfMonth(14);
            paymentDate=today.withDayOfMonth(15);
            dayStr = "15th";
        } else {
            periodStart = today.withDayOfMonth(15);
            periodEnd = today.withDayOfMonth(lastDayOfMonth - 1);

            paymentDate = today.withDayOfMonth(lastDayOfMonth);
            dayStr = lastDayOfMonth + "th";
        }

        log.info("Checking collections from {} to {} for payment on {}", periodStart, periodEnd, paymentDate);

        int supplierCount = collectMilkRepository.countSuppliersWithCollections(periodStart, periodEnd);
        if (supplierCount <= 0) {
            log.info("No suppliers found for period {} to {}", periodStart, periodEnd);
            return;
        }

        List<AdminEntity> admins = adminRepository.findAll();
        for (AdminEntity admin : admins) {
            if (notificationRepository.existsAdvanceForPaymentDateByAdmin(admin.getAdminId(), paymentDate)) {
                log.info("Advance notification already exists for admin {} for payment date {}", admin.getAdminId(), paymentDate);
                continue;
            }

            NotificationEntity n = new NotificationEntity();
            n.setAdmin(admin);
            n.setNotificationType("ADVANCE");
            n.setIsRead(false);
            n.setPaymentDate(paymentDate);
            n.setMessage(supplierCount + " suppliers need to be paid on " + dayStr);

            if (notificationRepository.save(n))
                log.info("Created advance notification for admin {} for payment date {}", admin.getAdminId(), paymentDate);
            else
                log.error("Failed to create advance notification for admin {}", admin.getAdminId());
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
        int lastDayOfMonth = today.lengthOfMonth();

        if (dayOfMonth != 15 && dayOfMonth != lastDayOfMonth) {
            log.info("Not the 15th or last day ({}), skipping payment notification generation", lastDayOfMonth);
            return;
        }

        LocalDate periodStart;
        LocalDate periodEnd;

        if (dayOfMonth == 15) {
            periodStart = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());
            periodEnd = today.withDayOfMonth(14);
        } else {
            periodStart = today.withDayOfMonth(15);
            periodEnd = today.withDayOfMonth(lastDayOfMonth - 1);
        }

        log.info("Generating payment notifications for period {} to {}", periodStart, periodEnd);

        List<Object[]> supplierList = collectMilkRepository.getEntityForPaymentNotification(periodStart, periodEnd);
        List<AdminEntity> admins = adminRepository.findAll();

        for (Object[] row : supplierList) {
            SupplierEntity supplier = (SupplierEntity) row[0];
            Double totalAmount = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

            PaymentDetailsEntity paymentDetailsEntity = new PaymentDetailsEntity();
            paymentDetailsEntity.setSupplier(supplier);
            paymentDetailsEntity.setAdmin(null);
            paymentDetailsEntity.setPeriodStart(periodStart);
            paymentDetailsEntity.setPeriodEnd(periodEnd);
            paymentDetailsEntity.setTotalAmount(totalAmount);
            paymentDetailsEntity.setPaymentDate(today);

            if (paymentDetailsRepository.save(paymentDetailsEntity))
                log.info("Payment details saved for supplier {}", supplier.getSupplierId());
            else
                log.error("Failed to save payment details for supplier {}", supplier.getSupplierId());

            for (AdminEntity admin : admins) {
                NotificationEntity n = new NotificationEntity();
                n.setAdmin(admin);
                n.setSupplier(supplier);
                n.setNotificationType("PAYMENT");
                n.setIsRead(false);
                n.setMessage("Pay Rs." + totalAmount + " to " + supplier.getFirstName() + " " +
                        supplier.getLastName() + " ("+ periodStart + " to " + periodEnd +")");
                n.setPaymentDate(today);
                n.setAmount(totalAmount);

                if (notificationRepository.save(n))
                    log.info("Payment notification created for admin {}", admin.getAdminId());
                else
                    log.error("Failed to create payment notification for admin {}", admin.getAdminId());
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
                return emailSender.mailForSupplierPayment(supplierEntity,paymentDetailsEntity);
            }
        }else {
            log.error("payment details not updated");
        }
        return false;
    }

    @Override
    public Double getTotalAmountPaid(Integer supplierId) {
        log.info("getTotalAmountPaid method in payment notification service");
        return paymentDetailsRepository.getTotalPaidAmount(supplierId);
    }

    @Override
    public List<PaymentDetailsDTO> getPaymentDetailsForSupplier(SupplierDTO supplierDTO) {
        log.info("getPaymentDetailsForSupplier method in payment notification service");
        List<PaymentDetailsEntity> paymentDetailsEntities=paymentDetailsRepository.getPaymentDetailsForSupplier(supplierDTO.getSupplierId());
        List<PaymentDetailsDTO> paymentDetailsDTOS=new ArrayList<>();
        paymentDetailsEntities.forEach(paymentDetailsEntity -> {
            PaymentDetailsDTO paymentDetailsDTO=new PaymentDetailsDTO();
            BeanUtils.copyProperties(paymentDetailsEntity,paymentDetailsDTO);
            paymentDetailsDTOS.add(paymentDetailsDTO);
        });
        return paymentDetailsDTOS;
    }

    @Override
    public boolean getPaymentDetailsForAdminEmailSummary() {
        log.info("getPaymentDetailsForAdminEmailSummary method in payment service");
        List<PaymentDetailsDTO> list=new ArrayList<>();
        List<PaymentDetailsEntity> paymentDetailsEntities=paymentDetailsRepository.getPaymentDetailsForAdminSummaryEmail();
        paymentDetailsEntities.forEach(paymentDetailsEntity -> {
            PaymentDetailsDTO paymentDetailsDTO=new PaymentDetailsDTO();
            BeanUtils.copyProperties(paymentDetailsEntity,paymentDetailsDTO);
            list.add(paymentDetailsDTO);
        });
        return emailSender.mailForAdminPaymentSummary(list);
    }

    @Override
    public List<PaymentDetailsDTO> getAllPaymentDetailsForAdminHistory(int page, int size) {
        log.info("getAllPaymentDetailsForAdminHistory method in payment service");
        List<PaymentDetailsEntity> paymentDetailsEntities=paymentDetailsRepository.getAllPaymentDetailsForAdminHistory(page,size);
        List<PaymentDetailsDTO> paymentDetailsDTOS=new ArrayList<>();
        paymentDetailsEntities.forEach(paymentDetailsEntity -> {
            PaymentDetailsDTO paymentDetailsDTO=new PaymentDetailsDTO();
            BeanUtils.copyProperties(paymentDetailsEntity,paymentDetailsDTO);
            if(paymentDetailsEntity.getSupplier()!=null) {
                SupplierDTO supplierDTO = new SupplierDTO();
                BeanUtils.copyProperties(paymentDetailsEntity.getSupplier(), supplierDTO);
                paymentDetailsDTO.setSupplier(supplierDTO);
            }
            if(paymentDetailsEntity.getAdmin()!=null)
            {
                AdminDTO adminDTO=new AdminDTO();
                BeanUtils.copyProperties(paymentDetailsEntity.getAdmin(),adminDTO);
                paymentDetailsDTO.setAdmin(adminDTO);
            }
            paymentDetailsDTOS.add(paymentDetailsDTO);
        });
        return paymentDetailsDTOS;
    }

    @Override
    public Integer getTotalCount() {
        log.info("getTotalCount method in payment service");
        return paymentDetailsRepository.getTotalCount();
    }
}
