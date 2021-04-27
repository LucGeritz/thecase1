package ninja.pinhole.model;

import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
public class ProductDao implements Dao<Product>{

    private EntityManager em;

    public ProductDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find all users.
     */
    public List<Product> findAll() {
        log.debug("Finding all users");
        List<Product> prods = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        log.debug(String.format("Found %s products", prods.size()));
        return prods;
    }

    /**
     * Insert a single user.
     */
    public void insert(Product p) {
        log.debug(String.format("Inserting %s", p.toString()));
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    /**
     * Remove all users!
     */
    public void removeAll() {
        log.debug(String.format("Removing all products!"));
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Product").executeUpdate();
        em.getTransaction().commit();
    }

    public void update(Product p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public Product find(long id){
        List<Product> pp = em.createNamedQuery("Product.findById", Product.class)
                .setParameter("id", id)
                .getResultList();
        return pp.size() > 0 ? pp.get(0) : null;
    }

    // Use a named query
    public List<Product> findByName(String name) {
        // Note use of parameter! https://www.baeldung.com/jpa-query-parameters
        return em.createNamedQuery("Product.findByName", Product.class)
                .setParameter("name", name)
                .getResultList();
    }

}