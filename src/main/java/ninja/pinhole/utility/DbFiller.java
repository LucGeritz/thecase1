package ninja.pinhole.utility;

import ninja.pinhole.model.Advertisement;
import ninja.pinhole.model.AdvertisementDao;
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

        AdvertisementDao ad = new AdvertisementDao(em);
        UserDao ud = new UserDao(em);

        // note must remove adverts first!
        ad.removeAll();
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

        //----«ADVERTS»-----------
        String[] advNames = new String[]{
                "Oma fiets", "Naaimachine", "Kladblokken",
                "Handboog met pijlen", "Luchtbuks", "Winterjas",
                "Strijkkwartet", "Voetbalspel", "Poker kaarten",
                "Broodmachine", "Rashond", "WiFi-Router"
        };

        String descr = " is zo goed als nieuw. Zien is kopen!";

        int userCount = 0;
        for (String name : advNames) {
            String longDescr = "Deze ".concat(name).concat(descr);
            var p = Advertisement.builder().
                    price(BigDecimal.valueOf(157.23))
                    .name(name)
                    .description(longDescr)
                    .build();
            ad.insert(p);
            long userId = userIds.get(userCount++%userIds.size());
            p.setUser(ud.find(userId));
            ad.update(p);
        }

        ad.findAll().forEach(System.out::println);

    }
}
