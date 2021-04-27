package ninja.pinhole.utility;

import ninja.pinhole.model.Product;
import ninja.pinhole.model.ProductDao;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DbFiller {
    private static final EntityManager em = Persistence
            .createEntityManagerFactory("MySQL-thecase")
            .createEntityManager();

    /*
    admin user          : admin / admin
    blocked normal user : user2 / user2
     */
    public static void main(String[] args) {

        ProductDao pd = new ProductDao(em);
        UserDao ud = new UserDao(em);

        // note must remove products first!
        pd.removeAll();
        ud.removeAll();

        String[] userNames = new String[]{
                "admin", "archie", "betsy", "rolph", "dewy", "fritz", "carla"
        };

        ArrayList<Long> userIds = new ArrayList<>();

        for (String name : userNames) {
            var u = User.builder()
                    .alias(name)
                    .password(name)
                    .email(name + "@thecase.nl")
                    .isAdmin(name.equals("admin"))
                    .blocked(name.equals("betsy"))
                    .build();
            ud.insert(u);

            userIds.add(u.getId());
        }

        List<User> users = ud.findAll();
        users.forEach(System.out::println);

        //----«PRODUCTS»-----------
        String[] prodNames = new String[]{
                "Oma fiets", "Naaimachine", "Kladblokken",
                "Handboog met pijlen", "Luchtbuks", "Winterjas",
                "Strijkkwartet", "Voetbalspel", "Poker kaarten",
                "Broodmachine", "Rashond", "WiFi-Router"
        };

        String descr = " is zo goed als nieuw. Zien is kopen!";

        int userCount = 0;
        for (String name : prodNames) {
            String longDescr = "Deze ".concat(name).concat(descr);
            var p = Product.builder().
                    price(BigDecimal.valueOf(157.23))
                    .name(name)
                    .description(longDescr)
                    .build();
            pd.insert(p);
            long userId = userIds.get(userCount++%userIds.size());
            p.setUser(ud.find(userId));
            pd.update(p);
        }

        pd.findAll().forEach(System.out::println);

    }
}
