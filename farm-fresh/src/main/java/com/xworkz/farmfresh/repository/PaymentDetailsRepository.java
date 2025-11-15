package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.PaymentDetailsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentDetailsRepository {
     boolean save(PaymentDetailsEntity entity);
     PaymentDetailsEntity getEntityBySupplierIdAndPaymentDate(LocalDate paymentDate, Integer supplierId, LocalDateTime createdDate);
     boolean update(PaymentDetailsEntity paymentDetailsEntity);
     Double getTotalPaidAmount(Integer supplierId);
    List<PaymentDetailsEntity> getPaymentDetailsForSupplier(Integer id);
    List<PaymentDetailsEntity> getPaymentDetailsForAdminSummaryEmail();
    List<PaymentDetailsEntity> getAllPaymentDetailsForAdminHistory(int page,int size);
    Integer getTotalCount();
    PaymentDetailsEntity getPaymentDetailsById(Integer id);
    Double getTotalPaymentsThisMonth();
    List<PaymentDetailsEntity> getRecentPayments();
    Double totalPendingAmount();
}
