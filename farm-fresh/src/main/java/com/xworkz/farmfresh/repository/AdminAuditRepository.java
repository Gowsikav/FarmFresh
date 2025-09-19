package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminAuditEntity;

import java.util.Optional;

public interface AdminAuditRepository {

    boolean save(AdminAuditEntity adminAuditEntity);
    Optional<AdminAuditEntity> findActiveSession(Integer adminId);
}

