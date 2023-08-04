package dao.hibernate;

import dao.ProductDao;
import models.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import java.util.List;
public class ProductOrmDao implements ProductDao {

    SessionFactory sessionFactory;

    public ProductOrmDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Product> getAllProducts() {
        Session session = sessionFactory.openSession();
        List<Product> products = session.createQuery("from Product").list();
        session.close();
        return products;
    }

    @Override
    public Product getProductById(int productId) {
        Session session = sessionFactory.openSession();
        Product product = session.get(Product.class, productId);
        session.close();
        return product;
    }

    @Override
    public void createProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(product);
        tx.commit();
        session.close();
    }

    @Override
    public void updateProduct(Product product) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(product);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteProduct(int productId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Product product = session.get(Product.class, productId);
        session.delete(product);
        tx.commit();
        session.close();
    }
}
