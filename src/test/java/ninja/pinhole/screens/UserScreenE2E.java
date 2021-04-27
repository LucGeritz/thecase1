package ninja.pinhole.screens;

import ninja.pinhole.console.AutoConsole;
import ninja.pinhole.model.ProductDao;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserScreenE2E {

    private static final int initialReccount = 10;
    private static Container container;
    private static final int waitTime = 0; // milliseconds
    private static Map<String, Long> alias2Id;

    @BeforeAll
    static void init() {
        // setup dependencies
        container = new Container();
        // entity manager
        container.<EntityManager>set("em",
                () -> Persistence
                        .createEntityManagerFactory("H2-thecase")
                        .createEntityManager()
        );
        // and LoginService
        container.<LoginService>set("lis",
                () -> new LoginService(
                        container.get("em")
                )
        );

        initDB();

    }

    @BeforeEach
    void setup() {
        // Always logged in as admin
        container.<LoginService>get("lis").login("user1", "geheim1");
    }

    @Test
    void WhenAdminBlocksUnblockedUserThenUserIsBlocked() {

        // Must be logged in!
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isTrue();

        AutoConsole console = setPathToBlockUserScreen();

        // block user 4
        long id = alias2Id.get("user4").longValue();

        User u = getUser(id);
        // user has to be initially unblocked to make this test useful
        Assertions.assertThat(u.isBlocked()).isFalse();

        console.buffer(id+"")
                .buffer("x") // exit user screen
                .buffer("x"); // exit main screen
        // go!
        new MainScreen(container, console).show();

        // should be blocked now
        u = getUser(id);
        Assertions.assertThat(u.isBlocked()).isTrue();
    }
    @Test
    void WhenAdminBlocksBlockedUserThenUserIsUnblocked() {

        // Must be logged in!
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isTrue();

        AutoConsole console = setPathToBlockUserScreen();

        // unblock user 5
        long id = alias2Id.get("user5").longValue();

        User u = getUser(id);
        // user has to be initially blocked to make this test useful
        Assertions.assertThat(u.isBlocked()).isTrue();

        console.buffer(id+"")
                .buffer("x") // exit user screen
                .buffer("x"); // exit main screen
        // go!
        new MainScreen(container, console).show();

        // should be UNblocked now
        u = getUser(id);
        Assertions.assertThat(u.isBlocked()).isFalse();
    }

    private AutoConsole setPathToBlockUserScreen() {
        AutoConsole console = new AutoConsole();

        console.buffer("3") // pick user screen
                .buffer("4") // pick block user
                .setWait(waitTime);

        return console;
    }

    private User getUser(long id){
        return new UserDao(container.get("em")).find(id);
    }

    private static void initDB() {

        // This is not about products but still I have to remove
        // them if any are there to remove all users!
        new ProductDao(container.get("em")).removeAll();

        UserDao ud = new UserDao(container.get("em"));
        ud.removeAll();

        alias2Id = new HashMap<String, Long>();

        // user1/geheim1 is admin
        // user5/geheim5 is blocked!
        for (var i = 0; i < initialReccount; i++) {
            User u = User.builder().alias("user" + i).email("j" + i + "@j.nl").password("geheim" + i).build();
            switch (i) {
                case 1:
                    u.setAdmin(true);
                    break;
                case 5:
                    u.setBlocked(true);
                    break;
            }
            ud.insert(u);
            alias2Id.put(u.getAlias(), u.getId());
        }
    }
}