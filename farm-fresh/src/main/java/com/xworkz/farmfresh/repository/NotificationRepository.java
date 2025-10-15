package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.NotificationEntity;

import java.time.LocalDate;
import java.util.List;

public interface NotificationRepository {
    boolean save(NotificationEntity entity);
    boolean existsAdvanceForPaymentDateByAdmin(Integer adminId, LocalDate paymentDate);
    List<NotificationEntity> findByAdminOrderByCreatedAtDesc(Integer adminId);
    boolean markAsRead(Long notificationId);
}
