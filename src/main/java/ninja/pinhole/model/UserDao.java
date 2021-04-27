package ninja.pinhole.model;

import lombok.extern.log4j.Log4j2;

import javax.persistence.EntityManager;
import java.util.List;


@Log4j2
public class UserDao implements Dao<User>{

    private EntityManager em;

    public UserDao(EntityManager em) {
        this.em = em;
    }

    /**
     * Find all users.
     */
    public List<User> findAll() {
        log.debug("Finding all users");
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        log.debug(String.format("Found %s users", users.size()));
        return users;
    }

    /**
     * Insert a single user.
     */
    public void insert(User u) {
        log.debug(String.format("Inserting %s", u.toString()));
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
    }

    /**
     * Remove all users!
     */
    public void removeAll() {
        log.debug(String.format("Removing all users!"));
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
    }

    /**
     * Find a user by alias.
     *
     * @param alias
     * @return USer or null if not found
     */
    public User findByAlias(String alias) {
        var users = em.createNamedQuery("User.findByAlias", User.class)
                .setParameter("alias", alias)
                .getResultList();
        return users.size()==1 ? users.get(0) : null;
    }

    public User find(long id){
        List<User> uu = em.createNamedQuery("User.findById", User.class)
                .setParameter("id", id)
                .getResultList();
        return uu.size() > 0 ? uu.get(0) : null;
    }


    public void update(User u) {
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
    }

}