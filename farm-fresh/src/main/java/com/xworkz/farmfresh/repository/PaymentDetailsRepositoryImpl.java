package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<PaymentDetailsEntity> getPaymentDetailsForSupplier(Integer id) {
        log.info("getPaymentDetailsForSupplier method in payment service");
        EntityManager entityManager=null;
        List<PaymentDetailsEntity> list=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createQuery("select a from PaymentDetailsEntity a where a.supplier.supplierId=:id order by id desc", PaymentDetailsEntity.class)
                    .setParameter("id",id)
                    .getResultList();
            return list;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return list;
    }

    @Override
    public List<PaymentDetailsEntity> getPaymentDetailsForAdminSummaryEmail() {
        log.info("getPaymentDetailsForAdminSummaryEmail method in payment service");
        EntityManager entityManager=null;
        List<PaymentDetailsEntity> list=new ArrayList<>();
        try {
            entityManager=entityManagerFactory.createEntityManager();
            LocalDate today = LocalDate.now();
            list = entityManager.createQuery(
                            "SELECT a FROM PaymentDetailsEntity a JOIN FETCH a.supplier WHERE a.paymentDate = :today",
                            PaymentDetailsEntity.class)
                    .setParameter("today", today)
                    .getResultList();
            return list;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if (entityManager != null && entityManager.isOpen()) {
                log.info("EntityManager is closed");
                entityManager.close();
            }
        }
        return list;
    }

    @Override
    public List<PaymentDetailsEntity> getAllPaymentDetailsForAdminHistory(int page, int size) {
        log.info("getAllPaymentDetailsForAdminHistory method in payment repository");
        EntityManager entityManager=null;
        List<PaymentDetailsEntity> list=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createQuery("select a from PaymentDetailsEntity a left join fetch a.supplier left join fetch a.admin order by a.paymentDate desc", PaymentDetailsEntity.class)
                    .setFirstResult((page-1)*size)
                    .setMaxResults(size).getResultList();
            return list;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return list;
    }

    @Override
    public Integer getTotalCount() {
        log.info("getTotalCount method in payment repo");
        EntityManager entityManager=null;
        Long count=0L;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            count=entityManager.createQuery("select count(a) from PaymentDetailsEntity a", Long.class).getSingleResult();
            return count.intValue();
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return count.intValue();
    }

    @Override
    public PaymentDetailsEntity getPaymentDetailsById(Integer id) {
        log.info("getPaymentDetailsById method in payment repository");
        EntityManager entityManager=null;
        PaymentDetailsEntity paymentDetailsEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            paymentDetailsEntity=entityManager.find(PaymentDetailsEntity.class,id);
            return paymentDetailsEntity;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return paymentDetailsEntity;
    }
}
