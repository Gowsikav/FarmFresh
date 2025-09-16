package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

@Slf4j
@Repository
public class AdminRepositoryImpl implements AdminRepository{

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public AdminRepositoryImpl()
    {
        log.info("AdminRepository implementation constructor");
    }

    @Override
    public boolean save(AdminEntity adminEntity) {
        log.info("save method in repo");
        log.info("repo data: {} ",adminEntity);
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(adminEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed");
            }
        }
        finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("EntityManager is closed");
            }
        }
        return false;
    }

    @Override
    public AdminEntity getDetailsByEmail(String email) {
        log.info("getDetailsByEmail method in repository");
        EntityManager entityManager=null;
        AdminEntity adminEntity=null;
        try
        {
            entityManager=entityManagerFactory.createEntityManager();
            adminEntity=(AdminEntity) entityManager.createNamedQuery("getDetailsByEmail").setParameter("email",email).getSingleResult();
            return adminEntity;
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
        return adminEntity;
    }

    @Override
    public boolean updateAdminProfileByEmail(String email, String adminName, String phoneNumber, String profilePath) {
        log.info("updateAdminProfileByEmail method in repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        AdminEntity existingEntity;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            existingEntity=(AdminEntity) entityManager.createNamedQuery("getDetailsByEmail").setParameter("email",email).getSingleResult();
            if(existingEntity==null)
            {
                return false;
            }
            existingEntity.setAdminName(adminName);
            existingEntity.setPhoneNumber(phoneNumber);
            if(profilePath!=null && !profilePath.isEmpty()) {
                existingEntity.setProfilePath(profilePath);
            }
            entityManager.merge(existingEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed");
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
    public boolean updateIsBlockedByEmail(String email, boolean isBlocked) {
        log.info("updateIsBlockedByEmail method in repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        AdminEntity existingEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            existingEntity=(AdminEntity) entityManager.createNamedQuery("getDetailsByEmail").setParameter("email",email).getSingleResult();
            if(existingEntity==null)
                return false;
            existingEntity.setIsBlocked(isBlocked);
            entityManager.merge(existingEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed");
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
    public boolean resetPasswordByEmail(String email, String password, String confirmPassword) {

        log.info("resetPasswordByEmail method in repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        AdminEntity existingEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            existingEntity=(AdminEntity) entityManager.createNamedQuery("getDetailsByEmail").setParameter("email",email).getSingleResult();
            if(existingEntity==null)
                return false;
            existingEntity.setPassword(password);
            existingEntity.setConfirmPassword(confirmPassword);
            existingEntity.setIsBlocked(false);
            entityManager.merge(existingEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed");
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
}
