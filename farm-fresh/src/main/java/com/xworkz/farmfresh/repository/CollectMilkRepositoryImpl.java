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
}
