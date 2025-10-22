package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.PaymentDetailsEntity;

import java.time.LocalDate;
import java.util.List;

public interface PaymentDetailsRepository {
     boolean save(PaymentDetailsEntity entity);
     PaymentDetailsEntity getEntityBySupplierIdAndPaymentDate(LocalDate paymentDate,Integer supplierId);
     boolean update(PaymentDetailsEntity paymentDetailsEntity);
     Double getTotalPaidAmount(Integer supplierId);
    List<PaymentDetailsEntity> getPaymentDetailsForSupplier(Integer id);
    List<PaymentDetailsEntity> getPaymentDetailsForAdminSummaryEmail();
}
