package ninja.pinhole.utility;

import ninja.pinhole.model.*;

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

        AdvertisementDao ad = new AdvertisementDao(em);
        UserDao ud = new UserDao(em);

        // note must remove adverts first!
        ad.removeAll();
        ud.removeAll();


        // U S E R S
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

        // A D V E R T S
        String[] advNames = new String[]{
                "Oma fiets", "Zolder cleaning", "Kladblokken",
                "Kinderfeestje?", "Luchtbuks", "Bijles Java",
                "Strijkkwartet", "Uitlaatservice", "Poker kaarten",
                "Broodbakken", "Rashond", "Internet problemen"
        };

        String descr = " is zo goed als nieuw. Zien is kopen!";

        int userCount = 0;
        for (String name : advNames) {

            String longDescr = "Deze ".concat(name).concat(descr);
            Advertisement a;
            if ((userCount % 2) == 0) {
                // on evens let's create a product
                a = Product.builder()
                        .category(ProductCategory.random())
                        .name(name)
                        .description(longDescr)
                        .price(BigDecimal.valueOf(157.23))
                        .build();
            } else {
                // on odds let's create a service
                a = Service.builder()
                        .category(ServiceCategory.random())
                        .name(name)
                        .description(longDescr)
                        .price(BigDecimal.valueOf(157.23))
                        .build();
            }

            ad.insert(a);
            long userId = userIds.get(userCount++ % userIds.size());
            a.setUser(ud.find(userId));
            ad.update(a);
        }

        ad.findAll().forEach(System.out::println);

    }
}
