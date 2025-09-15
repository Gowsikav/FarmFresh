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
        System.out.println("AdminRepository implementation constructor");
    }

    @Override
    public boolean save(AdminEntity adminEntity) {
        System.out.println("save method in repo");
        System.out.println("repo data: "+adminEntity);
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
            System.out.println(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                System.out.println("roll backed");
            }
        }
        finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                System.out.println("EntityManager is closed");
            }
        }
        return false;
    }

    @Override
    public AdminEntity getPasswordByEmail(String email) {
        System.out.println("getPasswordByEmail method in repository");
        EntityManager entityManager=null;
        AdminEntity adminEntity=null;
        try
        {
            entityManager=entityManagerFactory.createEntityManager();
            adminEntity=(AdminEntity) entityManager.createNamedQuery("getPasswordByEmail").setParameter("email",email).getSingleResult();
            return adminEntity;
        }catch (PersistenceException e)
        {
            System.out.println(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                System.out.println("EntityManager is closed");
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
            existingEntity=(AdminEntity) entityManager.createNamedQuery("getPasswordByEmail").setParameter("email",email).getSingleResult();
            if(existingEntity==null)
            {
                return false;
            }
            existingEntity.setAdminName(adminName);
            existingEntity.setPhoneNumber(phoneNumber);
            if(profilePath!=null) {
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
}
