package ninja.pinhole.utility;

import ninja.pinhole.model.*;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DbFiller {

    /*
        admin user          : admin / admin
        blocked normal user : betsy / betsy
     */
    public static void main(String[] args) {
        new DbFiller().fill(Persistence
                .createEntityManagerFactory("MySQL-thecase")
                .createEntityManager()
        );
    }

    public void fill(EntityManager em) {

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


        int userCount = 0;
        for (String name : advNames) {

            String descr = "";
            switch (new Random().nextInt(4)) {
                case 0:
                    descr = " is zo goed als nieuw.";
                    break;
                case 1:
                    descr = " heeft altijd binnen gestaan.";
                    break;
                case 2:
                    descr = " gaat weg tegen het hoogste bod.";
                    break;
                case 3:
                    descr = " is ook geschikt voor op de camping";
                    break;
            }
            descr = "Deze ".concat(name).concat(descr);
            Advertisement a;
            if ((userCount % 2) == 0) {
                // on evens let's create a product
                a = Product.builder()
                        .category(ProductCategory.random())
                        .name(name)
                        .description(descr)
                        .price(BigDecimal.valueOf(157.23))
                        .build();
            } else {
                // on odds let's create a service
                a = Service.builder()
                        .category(ServiceCategory.random())
                        .name(name)
                        .description(descr)
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
