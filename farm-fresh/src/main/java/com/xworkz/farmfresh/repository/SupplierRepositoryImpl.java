package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.SupplierEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public SupplierRepositoryImpl()
    {
     log.info("SupplierRepositoryImpl constructor");
    }

    @Override
    public boolean addSupplier(SupplierEntity supplierEntity) {
        log.info("add Supplier method in SupplierRepositoryImpl");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(supplierEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.warn("roll backed");
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
    public List<SupplierEntity> getAllSuppliers(int pageNumber,int pageSize) {
        log.info("getAllSuppliers method in supplier repository");
        log.info("page number {} page size {}",pageNumber,pageSize);
        EntityManager entityManager=null;
        List<SupplierEntity> supplierEntities=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntities=entityManager.createNamedQuery("getAllSuppliers")
                    .setFirstResult((pageNumber-1)*pageSize).setMaxResults(pageSize).getResultList();
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
        return supplierEntities;
    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkEmail method in SupplierRepository");
        EntityManager entityManager=null;
        SupplierEntity supplierEntity;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntity=(SupplierEntity) entityManager.createNamedQuery("checkEmail").setParameter("email",email).getSingleResult();
            return supplierEntity != null;
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
        return false;
    }

    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        log.info("checkPhoneNumber method in SupplierRepository");
        EntityManager entityManager=null;
        SupplierEntity supplierEntity;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntity=(SupplierEntity) entityManager.createNamedQuery("checkPhoneNumber").setParameter("phoneNumber",phoneNumber).getSingleResult();
            return supplierEntity != null;
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
        return false;
    }

    @Override
    public SupplierEntity getSupplierByEmail(String email) {
        log.info("getSupplierByEmail method in SupplierRepository");
        EntityManager entityManager=null;
        SupplierEntity supplierEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntity=(SupplierEntity) entityManager.createNamedQuery("checkEmail").setParameter("email",email).getSingleResult();
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
        return supplierEntity;
    }

    @Override
    public boolean updateSupplierDetails(SupplierEntity supplierEntity, Boolean isDelete) {
        log.info("updateSupplierDetailsBySupplier method in supplier repository");

        EntityManager entityManager = null;
        EntityTransaction entityTransaction = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            SupplierEntity existingEntity;
                existingEntity = (SupplierEntity) entityManager.createNamedQuery("checkEmail")
                        .setParameter("email", supplierEntity.getEmail())
                        .getSingleResult();
            if(existingEntity==null)
            {
                log.error("not found entity");
                return false;
            }

            if (isDelete) {
                existingEntity.setIsActive(false);
                existingEntity.setSupplierAuditEntity(supplierEntity.getSupplierAuditEntity());
            } else {
                existingEntity.setFirstName(supplierEntity.getFirstName());
                existingEntity.setLastName(supplierEntity.getLastName());
                existingEntity.setAddress(supplierEntity.getAddress());
                existingEntity.setTypeOfMilk(supplierEntity.getTypeOfMilk());
                existingEntity.setIsActive(true);
                existingEntity.setSupplierAuditEntity(supplierEntity.getSupplierAuditEntity());
            }

            entityManager.merge(existingEntity);
            entityTransaction.commit();
            log.info("Supplier details updated successfully");
            return true;

        } catch (PersistenceException e) {
            log.error("PersistenceException: " + e.getMessage());
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
                log.error("Rolled back");
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
    public Integer getSuppliersCount() {
        log.info("getSuppliersCount method in supplier repository");
        EntityManager entityManager=null;
        int count=0;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            Long count1=(Long) entityManager.createNamedQuery("getSuppliersCount").getSingleResult();
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
    public List<SupplierEntity> getSearchSuppliers(String keyword) {
        log.info("getSearchSuppliers method in supplier repository");
        EntityManager entityManager=null;
        List<SupplierEntity> list=null;
        try
        {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createNamedQuery("searchSuppliers").setParameter("keyword",keyword).getResultList();
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
    public SupplierEntity getSupplierByPhone(String phone) {
        log.info("getSupplierByPhone method in SupplierRepository");
        EntityManager entityManager=null;
        SupplierEntity supplierEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntity=(SupplierEntity) entityManager.createNamedQuery("checkPhoneNumber").setParameter("phoneNumber",phone).getSingleResult();
            return supplierEntity;
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
        return supplierEntity;
    }

    @Override
    public boolean setOTPAndTime(String email, String otp,LocalDateTime expiryTime) {
        log.info("setOTPAndTime method in supplier Repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        SupplierEntity existingEntity;
        try{
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            existingEntity = (SupplierEntity) entityManager.createNamedQuery("checkEmail")
                    .setParameter("email", email)
                    .getSingleResult();
            if(existingEntity==null)
            {
                log.error("entity not found");
                return false;
            }
            existingEntity.setLoginOTP(otp);
            existingEntity.setExpiryTime(expiryTime);
            entityManager.merge(existingEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("merge rollback");
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
    public boolean updateSupplierDetailsBySupplier(SupplierEntity supplierEntity) {
        log.info("updateSupplierDetailsBySupplier bySupplier method in supplier repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;

        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.merge(supplierEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("merge roll back");
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
    public SupplierEntity getSupplierDetailsAndBankById(Integer id) {
        log.info("getSupplierDetailsAndBankById bySupplier method in supplier repository");
        EntityManager entityManager=null;
        SupplierEntity supplierEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            supplierEntity=entityManager.createQuery("select a from SupplierEntity a left join fetch a.supplierBankDetails where a.supplierId=:id and a.isActive=true",
                    SupplierEntity.class)
                    .setParameter("id",id)
                    .getSingleResult();
            return supplierEntity;
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
        return supplierEntity;
    }
}
