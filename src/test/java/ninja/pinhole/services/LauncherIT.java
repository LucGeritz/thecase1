package ninja.pinhole.services;

import lombok.extern.log4j.Log4j2;
import ninja.pinhole.model.AdvertisementDao;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

class LauncherIT {

    private static Launcher target;
    private static EntityManager em;
    private static int initialRecCount = 10;
    private static LoginService lis;

    @BeforeEach
    void setup(){
        lis.logout();
    }

    @BeforeAll
    static void  init() {
        em = Persistence
                .createEntityManagerFactory("H2-thecase")
                .createEntityManager();
        initDB();
        lis = new LoginService(em);
        target = new Launcher(lis);
    }

    @Test
    void whenAnyOneClassIsCalledWithoutLoggedInItIsLaunched(){
        target.launch(new AnyOne());
        Assertions.assertThat(target.launchedOk()).isTrue();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("");
    }

    @Test
    void whenLoggedInClassIsCalledWithoutBeingLoggedInItIsNotLaunched(){
        target.launch(new LoggedIn());
        Assertions.assertThat(lis.isLoggedIn()).isFalse();
        Assertions.assertThat(target.launchedOk()).isFalse();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("U dient ingelogd te zijn");
    }

    @Test
    void whenLoggedInClassIsCalledWhileBeingLoggedInItIsLaunched(){
        lis.login("user1","geheim1");

        target.launch(new LoggedIn());

        Assertions.assertThat(lis.isLoggedIn()).isTrue();
        Assertions.assertThat(target.launchedOk()).isTrue();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("");
    }

    @Test
    void whenAdminClassIsCalledWithoutBeingLoggedInItIsNotLaunched(){
        target.launch(new Admin());
        Assertions.assertThat(lis.isLoggedIn()).isFalse();
        Assertions.assertThat(target.launchedOk()).isFalse();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("U dient ingelogd te zijn");
    }

    @Test
    void whenAdminClassIsCalledWhileBeingLoggedInAsNormalUserItIsNotLaunched(){
        lis.login("user1","geheim1");
        target.launch(new Admin());
        Assertions.assertThat(lis.isLoggedIn()).isTrue();
        Assertions.assertThat(target.launchedOk()).isFalse();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("U dient beheerder te zijn");
    }

    @Test
    void whenAdminClassIsCalledWhileBeingLoggedAsAdminItIsLaunched(){
        lis.login("user9","geheim9");
        target.launch(new Admin());

        Assertions.assertThat(lis.isLoggedIn()).isTrue();
        Assertions.assertThat(target.launchedOk()).isTrue();
        Assertions.assertThat(target.getErrorMsg()).isEqualTo("");
    }

    private static  void initDB(){
        User u;

        // make sure we start with empty db
        new AdvertisementDao(em).removeAll();
        new UserDao(em).removeAll();

        em.getTransaction().begin();

        // Available users are user0 to user9 with pw geheim0 to geheim9
        // user9 is admin
        for (var i =0; i<initialRecCount; i++){
            u = User.builder().alias("user"+i).isAdmin(false).email("j"+i+"@j.nl").password("geheim"+i).build();
            if (i==9){
                u.setAdmin(true);
            }
            em.persist(u);
        }
        em.getTransaction().commit();

    }
}

@Log4j2
class AnyOne implements Launchable{

    @Override
    public boolean needsAdminRights() {
        return false;
    }

    @Override
    public boolean needsLogIn() {
        return false;
    }

    @Override
    public void launch() {
        log.info("Launching "+AnyOne.class );
    }
}

@Log4j2
class LoggedIn implements Launchable{

    @Override
    public boolean needsAdminRights() {
        return false;
    }

    @Override
    public boolean needsLogIn() {
        return true;
    }

    @Override
    public void launch() {
        log.info("Launching "+LoggedIn.class );
    }
}

@Log4j2
class Admin implements Launchable{

    @Override
    public boolean needsAdminRights() {
        return true;
    }

    @Override
    public boolean needsLogIn() {
        return true;
    }

    @Override
    public void launch() {
        log.info("Launching "+Admin.class );
    }
}