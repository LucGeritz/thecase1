package ninja.pinhole.screens;

import ninja.pinhole.console.AutoConsole;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;
import ninja.pinhole.utility.DbFiller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

class BlockingUserE2E {

    private static final int initialReccount = 10;
    private static Container container;
    private static final int waitTime = 0; // milliseconds
    private static Map<String, Long> alias2Id = new HashMap<>();

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

        new DbFiller().fill(container.get("em"));

        // make lookup table from alias to id
        new UserDao(container.get("em")).findAll().forEach(user -> alias2Id.put(user.getAlias(), user.getId()));

    }

    @BeforeEach
    void setup() {
        // Always logged in as admin
        container.<LoginService>get("lis").login("admin", "admin");
    }

    @Test
    void WhenAdminBlocksUnblockedUserThenUserIsBlocked() {

        // Must be logged in!
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isTrue();

        AutoConsole console = setPathToBlockUserScreen();


        long id = alias2Id.get("rolph").longValue();

        User u = getUser(id);
        // user has to be initially unblocked to make this test useful
        Assertions.assertThat(u.isBlocked()).isFalse();

        console.buffer(id + "")
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

        // remember id blocked user
        long id = alias2Id.get("betsy");
        User u = getUser(id);

        // user has to be initially blocked to make this test useful
        Assertions.assertThat(u.isBlocked()).isTrue();

        console.buffer(id + "")
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

    private User getUser(long id) {
        return new UserDao(container.get("em")).find(id);
    }

}