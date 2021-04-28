package ninja.pinhole.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.SQLException;

class AdvertisementDaoIT {

    private static AdvertisementDao target;
    private static EntityManager em;
    private static int initialRecCount = 10;

    @BeforeAll
    static void init() throws SQLException {
        em = Persistence
                .createEntityManagerFactory("H2-thecase")
                .createEntityManager();
        target = new AdvertisementDao(em);
        initDB();
    }

    @Test
    void whenAdvertSearchedOnExistingIdThenAdvertIsReturned() {
        var p = target.findByName("advert1").get(0);
        long id = p.getId();
        p = target.find(id);

        Assertions.assertThat(p).isNotNull();
        Assertions.assertThat(p.getId()).isEqualTo(id);
    }

    @Test
    void whenAdvertSearchedOnNonExistingIdThenNullIsReturned() {

        var p = target.find(345345345);
        Assertions.assertThat(p).isNull();
    }

    @Test
    void whenAdvertSearchedOnExistingNameThenAdvertIsReturned() {
        var p = target.findByName("advert1").get(0);
        Assertions.assertThat(p).isNotNull();
        Assertions.assertThat(p.getName()).isEqualTo("advert1");
    }

    @Test
    void whenFindAllThenListOfAdvertsIsReturned() {
        var pp = target.findAll();
        Assertions.assertThat(pp.size()).isGreaterThanOrEqualTo(initialRecCount);
    }

    @Test
    void whenAdvertInsertedThenFindAllReturnsMore() {
        int count1 = target.findAll().size();
        var p = Advertisement.builder().name("Jo!").build();
        target.insert(p);
        Assertions.assertThat(target.findAll().size()).isGreaterThan(count1);
    }

    @Test
    void whenAdvertSearchedOnNoneExistingNameThenEmptyListIsReturned() {
        var pp = target.findByName("zzzzzzz");
        Assertions.assertThat(pp.size()).isZero();
    }

    private static void initDB() {

        target.removeAll();
        var ud = new UserDao(em);
        ud.removeAll();

        // make a user advert belongs to
        User u = User.builder()
                .alias("user1")
                .blocked(false)
                .email("a@b")
                .password("?")
                .build();
        ud.insert(u);

        for (var i = 0; i < initialRecCount; i++) {
            Advertisement p = Advertisement.builder()
                    .name("advert" + i)
                    .description("advert" + i + " is very good.")
                    .user(u)
                    .build();
            target.insert(p);
        }

    }

}