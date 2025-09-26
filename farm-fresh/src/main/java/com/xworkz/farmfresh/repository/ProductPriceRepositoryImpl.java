package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.ProductPriceEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.List;

@Slf4j
@Repository
public class ProductPriceRepositoryImpl implements ProductPriceRepository{

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public ProductPriceRepositoryImpl()
    {
        log.info("ProductPriceRepositoryImpl constructor");
    }

    @Override
    public boolean saveProduct(ProductPriceEntity entity) {
        log.info("save product method in product price repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entityManager.persist(entity);
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
                log.info("Entity manager is closed");
            }
        }
        return false;
    }

    @Override
    public List<ProductPriceEntity> getAllDetails() {
        log.info("getAll Details method in product price repository");
        EntityManager entityManager=null;
        List<ProductPriceEntity> list=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            list=entityManager.createNamedQuery("getAllDetails").getResultList();
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
        }finally {
            if(entityManager!=null && entityManager.isOpen())
            {
                entityManager.close();
                log.info("Entity manager is closed");
            }
        }
        return list;
    }

    @Override
    public boolean updateProduct(ProductPriceEntity productPriceEntity) {
        log.info("updateProduct method in product price repository");
        log.info("edit{}",productPriceEntity);
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        ProductPriceEntity existingEntity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            existingEntity=entityManager.find(ProductPriceEntity.class,productPriceEntity.getProductId());
            if(existingEntity==null)
            {
                return false;
            }
            existingEntity.setUpdatedBy(productPriceEntity.getUpdatedBy());
            existingEntity.setUpdatedAt(productPriceEntity.getUpdatedAt());
            existingEntity.setPrice(productPriceEntity.getPrice());
            existingEntity.setProductName(productPriceEntity.getProductName());
            entityManager.merge(existingEntity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed the update");
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
    public boolean deleteProduct(Integer productId) {
        log.info("deleteProduct method in product price repository");
        EntityManager entityManager=null;
        EntityTransaction entityTransaction=null;
        ProductPriceEntity entity=null;
        try {
            entityManager=entityManagerFactory.createEntityManager();
            entityTransaction=entityManager.getTransaction();
            entityTransaction.begin();
            entity=entityManager.find(ProductPriceEntity.class,productId);
            if(entity==null)
                return false;
            entity.setIsActive(false);
            entityManager.merge(entity);
            entityTransaction.commit();
            return true;
        }catch (PersistenceException e)
        {
            log.error(e.getMessage());
            if(entityTransaction!=null)
            {
                entityTransaction.rollback();
                log.error("roll backed the delete");
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
