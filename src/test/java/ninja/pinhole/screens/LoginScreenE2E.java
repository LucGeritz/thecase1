package ninja.pinhole.screens;

import ninja.pinhole.console.AutoConsole;
import ninja.pinhole.model.AdvertisementDao;
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


class LoginScreenE2E {

    private static final int initialReccount = 10;
    private static Container container;
    private static final int waitTime = 0; // milliseconds

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
        container.<LoginService>get("lis").logout();
    }

    @Test
    void WhenUserLogsInWithValidCredentialsThenUserIsLoggedIn() {
        // given: user not logged in

        // when: user logs in with valid vredentials
        AutoConsole console = setupLoginBuffer("user2", "geheim2");
        MainScreen ms = new MainScreen(container, console);
        LoginService lis = container.get("lis");
        Assertions.assertThat(lis.isLoggedIn()).isFalse();
        ms.show();

        // then: user is logged in
        Assertions.assertThat(lis.isLoggedIn()).isTrue();
        Assertions.assertThat(lis.getCurrentUserAlias()).isEqualTo("user2");
    }

    @Test
    void WhenUserLogsInWithInvalidCredentialsThenUserIsNotLoggedIn() {

        AutoConsole console = setupLoginBuffer("user2", "wrong_pw");
        MainScreen ms = new MainScreen(container, console);
        // start flow
        ms.show();
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isFalse();
    }

    @Test
    void WhenUserLogsInWithValidCredentialsButUserIsBlockedThenUserIsNotLoggedIn() {
        // given: user is logged out

        // when: user logs in with credentials of blocked user
        AutoConsole console = setupLoginBuffer("user3", "geheim3");
        MainScreen ms = new MainScreen(container, console);
        ms.show();

        // then: user is not logged in
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isFalse();
    }

    @Test
    void WhenLoggedInUserLogsOutThenUserIsLoggedOut() {

        // given: user is logged in
        LoginService lis = container.get("lis");
        lis.login("user1", "geheim1");
        Assertions.assertThat(lis.isLoggedIn()).isTrue();

        // when: user logs out
        AutoConsole console = new AutoConsole();
        console.buffer("1") // pick login
                .buffer("L") // do login/logout
                .buffer("x") // exit log screen
                .buffer("x") // exit main screen
                .setWait(waitTime);

        MainScreen ms = new MainScreen(container, console);
        ms.show();

        // then: user is logged out
        Assertions.assertThat(lis.isLoggedIn()).isFalse();

    }

    private AutoConsole setupLoginBuffer(String user, String pw) {

        AutoConsole console = new AutoConsole();

        console.buffer("1")      // pick login
                .buffer("1")  // pick naam
                .buffer(user) // enter naam
                .buffer("2") // pick ww
                .buffer(pw) // enter ww
                .buffer("L") // login!
                .buffer("x") // exit login
                .buffer("x") //  exit main
                .setWait(waitTime);

        return console;
    }

    private static void initDB() {
        // This is not about adverts but still I have to remove
        // them if any are there to remove all users!
        new AdvertisementDao(container.get("em")).removeAll();

        UserDao ud = new UserDao(container.get("em"));
        ud.removeAll();
        User u;

        // user3/geheim3 is blocked!
        for (var i = 0; i < initialReccount; i++) {
            u = User.builder().alias("user" + i).isAdmin(true).email("j" + i + "@j.nl").password("geheim" + i).build();
            switch (i) {
                case 3:
                    u.setBlocked(true);
                    break;
            }
            ud.insert(u);
        }
    }

}