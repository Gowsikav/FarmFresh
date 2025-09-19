package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminAuditEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
@Repository
public class AdminAuditRepositoryImpl implements AdminAuditRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public AdminAuditRepositoryImpl() {
        log.info("AdminAuditRepositoryImpl constructor");
    }

    @Override
    public boolean save(AdminAuditEntity adminAuditEntity) {
        log.info("save method in AdminAuditRepositoryImpl");
        EntityManager entityManager = null;
        EntityTransaction entityTransaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.merge(adminAuditEntity);
            entityTransaction.commit();
            return true;
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
            if (entityTransaction != null) {
                entityTransaction.rollback();
                log.error("Transaction rolled back");
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return false;
    }

    @Override
    public Optional<AdminAuditEntity> findActiveSession(Integer adminId) {
        log.info("findActiveSession method in AdminAuditRepositoryImpl for adminId {}", adminId);
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            AdminAuditEntity result = entityManager.createQuery(
                            "SELECT a FROM AdminAuditEntity a " +
                                    "WHERE a.adminEntity.id = :adminId " +
                                    "AND a.logoutTime IS NULL " +
                                    "ORDER BY a.loginTime DESC", AdminAuditEntity.class)
                    .setParameter("adminId", adminId)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error in findActiveSession: {}", e.getMessage(), e);
            return Optional.empty();
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
    }
}
