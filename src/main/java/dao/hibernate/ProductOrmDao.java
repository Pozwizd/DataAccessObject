package dao.hibernate;

import Entity.Product;
import utils.EntityManagerUtil;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
public class ProductOrmDao implements ProductDao {

    @Override
    public void createProduct(Product product) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(product);
            em.persist(product);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public List<Product> getAllProducts() {
        EntityManager em  = null;
        ArrayList<Product> products = new ArrayList<>();
        try {
            em = EntityManagerUtil.getEntityManager();
            products.add((Product) em.createQuery("from Product").getResultList());
            em.close();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return products;
    }

    @Override
    public Product getProductById(int productId) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            Product product = em.find(Product.class, productId);
            em.close();
            return product;
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    @Override
    public void updateProduct(Product product) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(product);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void deleteProduct(int productId) {
        EntityManager em = null;
        try {
            em = EntityManagerUtil.getEntityManager();
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);
            em.remove(product);
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
