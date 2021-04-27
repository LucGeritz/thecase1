package ninja.pinhole.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.SQLException;

class UserDaoIT {

    private static UserDao target;
    private static EntityManager em;
    private static Logger log = LoggerFactory.getLogger(UserDaoIT.class);
    private static int initialRecCount = 10;

    @BeforeAll
    static void init() throws SQLException {
        em = Persistence
                .createEntityManagerFactory("H2-thecase")
                .createEntityManager();
        target = new UserDao(em);
        initDB();

    }

    @Test
    void findAll() {
        var users = target.findAll();
        Assertions.assertThat(users.size()).isGreaterThanOrEqualTo(initialRecCount);
    }

    @Test
    void whenUserSearchedByAliasIsNotThereThenNullIsReturned() {
        var u = target.findByAlias("IDoNotExist");
        Assertions.assertThat(u).isNull();
    }
    @Test
    void whenUserSearchedByAliasThenUserIsReturned() {
        var u = target.findByAlias("user1");
        Assertions.assertThat(u).isNotNull();
        Assertions.assertThat(u.getAlias()).isEqualTo("user1");
    }

    @Test
    void whenUserSearchedByIdThenUserIsReturned() {
        var u = target.findByAlias("user1");
        long id = u.getId();
        target.find(id);
        Assertions.assertThat(u.getAlias()).isEqualTo("user1");
    }

    @Test
    void whenNewUserInsertedThenTotalUsersIsMore() {
        var count1 = target.findAll().size();
        var u = User.builder().alias("Smith").password("secret!").build();
        target.insert(u);
        Assertions.assertThat(target.findAll().size()).isGreaterThan(count1);
    }

    @Test
    void whenUserUpdatedThenChangesAreThereOnNextGet() {
        var u = target.findByAlias("user1");
        u.setPassword("jojo");
        long id = u.getId();
        target.update(u);
        Assertions.assertThat(target.find(id).getPassword()).isEqualTo("jojo");
    }



    private static void initDB() {

        target.removeAll();

        User u;

        for (var i = 0; i < initialRecCount; i++) {
            u = User.builder().alias("user" + i).isAdmin(true).email("j" + i + "@j.nl").password("geheim" + i).build();
            target.insert(u);
        }

    }

}