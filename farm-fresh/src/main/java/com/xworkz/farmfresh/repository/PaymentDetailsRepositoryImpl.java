package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;

@Slf4j
@Repository
public class PaymentDetailsRepositoryImpl implements PaymentDetailsRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public PaymentDetailsRepositoryImpl() {
        log.info("PaymentDetailsRepositoryImpl constructor");
    }

    @Override
    public boolean save(PaymentDetailsEntity entity) {
        log.info("Saving payment details entity");
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            log.error("Error saving payment details: {}", e.getMessage());
            if (transaction != null && transaction.isActive()) {
                log.error("roll back");
                transaction.rollback();
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return false;
    }

    @Override
    public PaymentDetailsEntity getEntityBySupplierIdAndPaymentDate(LocalDate paymentDate, Integer supplierId) {
        log.info("getEntityBySupplierIdAndPaymentDate method in payment repo");
        EntityManager entityManager=null;
        PaymentDetailsEntity paymentDetailsEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            paymentDetailsEntity=entityManager.createQuery("select a from PaymentDetailsEntity a where a.supplier.supplierId=:id and a.paymentDate=:paymentDate", PaymentDetailsEntity.class)
                    .setParameter("id",supplierId)
                    .setParameter("paymentDate",paymentDate)
                    .getSingleResult();
            return paymentDetailsEntity;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return null;
    }

    @Override
    public boolean update(PaymentDetailsEntity paymentDetailsEntity) {
        log.info("update method in payment repo");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.merge(paymentDetailsEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll back");
            }
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return false;
    }

    @Override
    public Double getTotalPaidAmount(Integer supplierId) {
        log.info("getTotalPaidAmount method in payment details repo");
        EntityManager entityManager=null;
        Double totalAmount=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            totalAmount=entityManager.createQuery("SELECT SUM(p.totalAmount) FROM PaymentDetailsEntity p WHERE p.supplier.supplierId = :id and p.paymentStatus='PAID'", Double.class)
                    .setParameter("id",supplierId).getSingleResult();
            return totalAmount;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return totalAmount;
    }
}
