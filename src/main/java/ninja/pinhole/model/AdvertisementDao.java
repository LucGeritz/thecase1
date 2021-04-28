package ninja.pinhole.model;

import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
public class AdvertisementDao implements Dao<Advertisement>{

    private EntityManager em;

    public AdvertisementDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find all users.
     */
    public List<Advertisement> findAll() {
        log.debug("Finding all users");
        List<Advertisement> prods = em.createQuery("SELECT p FROM Advertisement p", Advertisement.class).getResultList();
        log.debug(String.format("Found %s adverts", prods.size()));
        return prods;
    }

    /**
     * Insert a single user.
     */
    public void insert(Advertisement p) {
        log.debug(String.format("Inserting %s", p.toString()));
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    /**
     * Remove all users!
     */
    public void removeAll() {
        log.debug(String.format("Removing all adverts!"));
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Advertisement").executeUpdate();
        em.getTransaction().commit();
    }

    public void update(Advertisement p) {
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public Advertisement find(long id){
        List<Advertisement> pp = em.createNamedQuery("Adv.findById", Advertisement.class)
                .setParameter("id", id)
                .getResultList();
        return pp.size() > 0 ? pp.get(0) : null;
    }

    // Use a named query
    public List<Advertisement> findByName(String name) {
        // Note use of parameter! https://www.baeldung.com/jpa-query-parameters
        return em.createNamedQuery("Adv.findByName", Advertisement.class)
                .setParameter("name", name)
                .getResultList();
    }

}