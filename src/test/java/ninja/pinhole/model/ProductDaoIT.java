package ninja.pinhole.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.SQLException;

class ProductDaoIT {

    private static ProductDao target;
    private static EntityManager em;
    private static int initialRecCount = 10;

    @BeforeAll
    static void init() throws SQLException {
        em = Persistence
                .createEntityManagerFactory("H2-thecase")
                .createEntityManager();
        target = new ProductDao(em);
        initDB();
    }

    @Test
    void whenProductSearchedOnExistingIdThenProductIsReturned() {
        var p = target.findByName("product1").get(0);
        long id = p.getId();
        p = target.find(id);

        Assertions.assertThat(p).isNotNull();
        Assertions.assertThat(p.getId()).isEqualTo(id);
    }

    @Test
    void whenProductSearchedOnNonExistingIdThenNullIsReturned() {

        var p = target.find(345345345);
        Assertions.assertThat(p).isNull();
    }

    @Test
    void whenProductSearchedOnExistingNameThenProductIsReturned() {
        var p = target.findByName("product1").get(0);
        Assertions.assertThat(p).isNotNull();
        Assertions.assertThat(p.getName()).isEqualTo("product1");
    }

    @Test
    void whenFindAllThenListOfProductsIsReturned() {
        var pp = target.findAll();
        Assertions.assertThat(pp.size()).isGreaterThanOrEqualTo(initialRecCount);
    }

    @Test
    void whenProductInsertedThenFindAllReturnsMore() {
        int count1 = target.findAll().size();
        var p = Product.builder().name("Jo!").build();
        target.insert(p);
        Assertions.assertThat(target.findAll().size()).isGreaterThan(count1);
    }

    @Test
    void whenProductSearchedOnnONExistingNameThenEmptyListIsReturned() {
        var pp = target.findByName("zzzzzzz");
        Assertions.assertThat(pp.size()).isZero();
    }

    private static void initDB() {

        target.removeAll();
        var ud = new UserDao(em);
        ud.removeAll();

        // make a user product belongs to
        User u = User.builder()
                .alias("user1")
                .blocked(false)
                .email("a@b")
                .password("?")
                .build();
        ud.insert(u);

        for (var i = 0; i < initialRecCount; i++) {
            Product p = Product.builder()
                    .name("product" + i)
                    .description("product" + i + " is very good.")
                    .user(u)
                    .build();
            target.insert(p);
        }

    }

}