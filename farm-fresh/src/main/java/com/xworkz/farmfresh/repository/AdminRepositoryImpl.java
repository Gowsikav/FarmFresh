package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

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
}
