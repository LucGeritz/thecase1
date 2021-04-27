package ninja.pinhole.services;

import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;

import javax.persistence.EntityManager;

/**
 * Things for loggin in, logging out and logged-in state
 */
public class LoginService {

    private EntityManager em;
    private User currentUser;
    private UserDao ud;

    public LoginService(EntityManager em){
        this.em = em;
    }

    // Can I use an optional for this?
    public boolean login(String alias, String pw) {
        var u = getUserDao().findByAlias(alias);
        if(u == null || !(u.getPassword().equals(pw)) || u.isBlocked() ){
            // no user found or password differs
            return false;
        }

        // logged in! save current user
        this.currentUser = u;
        return true;
    }

    /**
     * Logout
     * Logging out while not being logged in is not considered an error
     */
    public void logout() {
        this.currentUser = null;
    }

    public boolean isCurrentUserAdmin() {
        return currentUser == null ? false : currentUser.isAdmin();
    }

    // return value null indicates 'not logged in'!
    // but better use isLoggedIn()!
    public User getCurrentUser() {
        return currentUser;
    }

    public String getCurrentUserAlias() {
        return currentUser == null ? "" : currentUser.getAlias();
    }

    public boolean isLoggedIn(){
        return !(currentUser == null);
    }

    private UserDao getUserDao(){
        if(this.ud == null){
            this.ud = new UserDao(em);
        }
        return this.ud;
    }
}