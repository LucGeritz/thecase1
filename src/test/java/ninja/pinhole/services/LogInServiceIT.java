package ninja.pinhole.services;

import ninja.pinhole.model.ProductDao;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

class LogInServiceIT {

    private static LoginService target;
    private static EntityManager em;
    private static int initialRecCount = 10;

    @BeforeEach
    void setup() {
        target.logout();
    }

    @BeforeAll
    static void init() {
        em = Persistence
                .createEntityManagerFactory("H2-thecase")
                .createEntityManager();
        initDB();
        target = new LoginService(em);
    }

    @Test
    void whenLogInSuccesfullThenLoggedInReturnsTrue() {
        target.login("user0", "geheim0");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
    }

    @Test
    void whenLoginSuccesfullThenCurrentUserIsFilled() {
        target.login("user0", "geheim0");
        var u = target.getCurrentUser();
        Assertions.assertThat(u).isNotNull();
        Assertions.assertThat(u.getAlias()).isEqualTo("user0");
    }

    @Test
    void whenLoginNotSuccesfullThenCurrentUserIsNull() {
        target.login("user0", "fout!!!");
        var u = target.getCurrentUser();
        Assertions.assertThat(u).isNull();
    }

    @Test
    void whenLogInNotSuccesfullThenLoggedInReturnsFalse() {
        target.login("user0", "fout!!!");
        Assertions.assertThat(target.isLoggedIn()).isFalse();
    }

    @Test
    void whenLogoutThenLoggedInReturnsFalse() {
        target.login("user0", "geheim0");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
        target.logout();
        Assertions.assertThat(target.isLoggedIn()).isFalse();
    }

    @Test
    void whenLogoutThenCurrentUserIsNull() {
        target.login("user0", "geheim0");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
        target.logout();
        Assertions.assertThat(target.getCurrentUser()).isNull();
    }

    @Test
    void whenAdminUserSuccessfulLoggedInThenIsAdminReturnsTrue() {
        target.login("user9", "geheim9");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
        Assertions.assertThat(target.isCurrentUserAdmin()).isTrue();
    }

    @Test
    void whenNormalUserSuccessfulLoggedInThenIsAdminReturnsFalse() {
        target.login("user8", "geheim8");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
        Assertions.assertThat(target.isCurrentUserAdmin()).isFalse();
    }

    @Test
    void whenUserSuccessfulLoggedInThenCurrentAliasReturnsAlias() {
        target.login("user1", "geheim1");
        Assertions.assertThat(target.isLoggedIn()).isTrue();
        String alias = target.getCurrentUserAlias();
        Assertions.assertThat(alias).isEqualTo("user1");
    }

    @Test
    void whenUserNotSuccessfulLoggedInThenCurrentAliasReturnsEmpty() {
        target.login("userX", "geheimX");
        Assertions.assertThat(target.isLoggedIn()).isFalse();
        String alias = target.getCurrentUserAlias();
        Assertions.assertThat(alias).isEqualTo("");
    }

    private static void initDB() {

        new ProductDao(em).removeAll();
        new UserDao(em).removeAll();

        User u;

        em.getTransaction().begin();

        // Available users are user0 to user9 with pw geheim0 to geheim9
        // user9 is admin
        for (var i = 0; i < initialRecCount; i++) {
            u = User.builder().alias("user" + i).isAdmin(false).email("j" + i + "@j.nl").password("geheim" + i).build();
            if (i == 9) {
                u.setAdmin(true);
            }
            em.persist(u);
        }
        em.getTransaction().commit();

    }
}