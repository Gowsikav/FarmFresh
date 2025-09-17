package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminAuditEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

@Slf4j
@Repository
public class AdminAuditRepositoryImpl implements AdminAuditRepository{

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public AdminAuditRepositoryImpl()
    {
        log.info("AdminAuditRepositoryImpl constructor");
    }

    @Override
    public boolean save(AdminAuditEntity adminAuditEntity) {
        log.info("save method in AdminAuditRepositoryImpl");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.merge(adminAuditEntity);
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
