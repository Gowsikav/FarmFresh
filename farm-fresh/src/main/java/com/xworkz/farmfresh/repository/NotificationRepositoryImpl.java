package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.NotificationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public NotificationRepositoryImpl()
    {
        log.info("NotificationRepositoryImpl constructor");
    }

    @Override
    public boolean save(NotificationEntity entity) {
        log.info("save method in NotificationRepositoryImpl");
        EntityManager entityManager = null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction= entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(entity);
            entityTransaction.commit();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (entityTransaction!=null) {
                log.error("roll back");
                entityTransaction.rollback();
            }

        } finally {
            if(entityManager!=null && entityManager.isOpen()) {
                entityManager.close();
                log.info("Entity Manager is closed");
            }
        }
        return false;
    }

    @Override
    public boolean existsAdvanceForPaymentDateByAdmin(Integer adminId, LocalDate paymentDate) {
        log.info("existsAdvanceForPaymentDateByAdmin method in NotificationRepositoryImpl");
        EntityManager entityManager=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            Long count = entityManager.createQuery(
                    "SELECT COUNT(n) FROM NotificationEntity n " +
                    "WHERE n.notificationType = :type AND n.paymentDate = :pdate AND n.admin.adminId = :aid",
                    Long.class)
                .setParameter("type", "ADVANCE")
                .setParameter("pdate", paymentDate)
                .setParameter("aid", adminId)
                .getSingleResult();
            return count != null && count > 0;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            return false;
        }
        finally {
            if(entityManager!=null && entityManager.isOpen()) {
                entityManager.close();
                log.info("Entity Manager is closed");
            }
        }
    }

    @Override
    public List<NotificationEntity> findByAdminOrderByCreatedAtDesc(Integer adminId) {
        log.info("findByAdminOrderByCreatedAtDesc method in notification repo");
        EntityManager entityManager = null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            return entityManager.createQuery(
                "SELECT n FROM NotificationEntity n " +
                "WHERE n.admin.adminId = :adminId and n.isRead=false " +
                "ORDER BY n.createdAt DESC",
                NotificationEntity.class
            )
            .setParameter("adminId", adminId)
            .getResultList();
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        } finally {
            if(entityManager!=null && entityManager.isOpen()) {
                entityManager.close();
                log.info("Entity Manager is closed");
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean markAsRead(Long notificationId) {
        log.info("mark as read method in notification repo");
        EntityManager entityManager = null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.createQuery("UPDATE NotificationEntity n SET n.isRead = true WHERE n.id = :id")
              .setParameter("id", notificationId)
              .executeUpdate();
            entityTransaction.commit();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (entityTransaction!=null) {
                entityTransaction.rollback();
                log.error("roll back");
            }
        } finally {
            if(entityManager!=null && entityManager.isOpen()) {
                entityManager.close();
                log.info("Entity Manager is closed");
            }
        }
        return false;
    }
}
