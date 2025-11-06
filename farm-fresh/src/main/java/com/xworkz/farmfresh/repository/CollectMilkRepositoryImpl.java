package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.CollectMilkEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
public class CollectMilkRepositoryImpl implements CollectMilkRepository{

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public CollectMilkRepositoryImpl()
    {
        log.info("CollectMilkRepository impl constructor");
    }

    @Override
    public boolean save(CollectMilkEntity collectMilkEntity) {
        log.info("save method in CollectMilkRepositoryImpl");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(collectMilkEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("Insert rollback");
            }
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return false;
    }

    @Override
    public List<CollectMilkEntity> getAllDetailsByDate(LocalDate selectDate) {
        log.info("getAllDetailsByDate method in collectMilkRepository");
        EntityManager entityManager=null;
        List<CollectMilkEntity> list=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createNamedQuery("getAllDetailsByDate").setParameter("selectDate",selectDate).getResultList();
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
    public List<CollectMilkEntity> getAllDetailsBySupplier(String email,int page,int size) {
        log.info("getAllDetailsBySupplier method in collectMilkRepository");
        EntityManager entityManager=null;
        List<CollectMilkEntity> list=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createNamedQuery("getAllDetailsByEmail").setParameter("email",email)
                    .setFirstResult((page-1)*size).setMaxResults(size).getResultList();
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
    public Integer getCountOFMilkDetailsByEmail(String email) {
        log.info("getCountOFMilkDetailsByEmail method in milk collect repository");
        EntityManager entityManager=null;
        int count=0;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            Long count1=(Long) entityManager.createNamedQuery("getMilkDetailsCountByEmail")
                    .setParameter("email",email)
                    .getSingleResult();
            count=count1.intValue();
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
        return count;
    }

    @Override
    public int countSuppliersWithCollections(LocalDate startDate, LocalDate endDate) {
        log.info("countSuppliersWithCollections method in collect milk repository");
        EntityManager entityManager=null;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            Long count = entityManager.createQuery(
                            "SELECT COUNT(DISTINCT c.supplier.supplierId) " +
                                    "FROM CollectMilkEntity c " +
                                    "WHERE c.collectedDate BETWEEN :start AND :end",
                            Long.class
                    )
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .getSingleResult();
            return (count == null) ? 0 : count.intValue();
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
        return 0;
    }

    @Override
    public List<Object[]> getEntityForPaymentNotification(LocalDate startDate, LocalDate endDate) {
        log.info("getEntityForPaymentNotification method in collectMilk repo");
        EntityManager entityManager=null;
        List<Object[]> list=null;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createQuery("select c.supplier, SUM(c.totalAmount) from CollectMilkEntity c " +
                    "where c.collectedDate between :start and :end group by c.supplier", Object[].class)
                    .setParameter("start",startDate)
                    .setParameter("end",endDate).getResultList();
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
    public List<CollectMilkEntity> getCollectMilkDetailsForSupplierById(Integer supplierId, LocalDate start,LocalDate end) {
        log.info("getCollectMilkDetailsForSupplierById method in collect milk repo");
        EntityManager entityManager=null;
        List<CollectMilkEntity> list=null;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createQuery("select a from CollectMilkEntity a where a.supplier.supplierId=:id and a.collectedDate between :start and :end order by a.collectMilkId desc", CollectMilkEntity.class)
                    .setParameter("id",supplierId)
                    .setParameter("end",end)
                    .setParameter("start",start)
                    .getResultList();
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
    public Double getTotalLitre(Integer supplierId) {
        log.info("getCollectMilkDetailsForSupplierById method in collect milk repo");
        EntityManager entityManager=null;
        Double totalLitre=0.0d;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            totalLitre=entityManager.createQuery("select SUM(c.quantity) FROM CollectMilkEntity c WHERE c.supplier.supplierId = :id",Double.class)
                    .setParameter("id",supplierId).getSingleResult();

            return totalLitre;
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
        return totalLitre;
    }

    @Override
    public LocalDate getLastCollectedDate(Integer supplierId) {
        log.info("getLastCollectedDate method in collect milk repo");
        EntityManager entityManager=null;
        LocalDate lastDate=null;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            lastDate=entityManager.createQuery("select MAX(c.collectedDate) FROM CollectMilkEntity c WHERE c.supplier.supplierId = :id",LocalDate.class)
                    .setParameter("id",supplierId).getSingleResult();

            return lastDate;
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
        return lastDate;
    }

    public Double getTotalMilkCollected() {
        log.info("getTotalMilkCollected method in collect milk repo");
        EntityManager entityManager=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            Double total = entityManager.createQuery("SELECT SUM(m.quantity) FROM CollectMilkEntity m", Double.class)
                    .getSingleResult();
            return total != null ? total : 0.0;
        }catch(PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return 0.0;
    }

    public List<CollectMilkEntity> getRecentCollections() {
        log.info("getRecentCollections method in collect milk repo");
        EntityManager entityManager=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            return entityManager.createQuery("SELECT m FROM CollectMilkEntity m join fetch m.supplier ORDER BY m.collectedDate DESC", CollectMilkEntity.class)
                    .setMaxResults(5)
                    .getResultList();
        } catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return null;
    }

    @Override
    public List<CollectMilkEntity> getAllEntityForExport() {
        log.info("getAllEntityForExport method in collect milk repo");
        EntityManager entityManager=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            return entityManager.createQuery("SELECT m FROM CollectMilkEntity m join fetch m.supplier join fetch m.admin order by m.collectMilkId", CollectMilkEntity.class)
                    .getResultList();
        } catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return null;
    }
}
