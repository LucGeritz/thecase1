package ninja.pinhole.screens;

import ninja.pinhole.console.AutoConsole;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;
import ninja.pinhole.utility.DbFiller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;


class LoggingInAndOutE2E {

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

        new DbFiller().fill(container.get("em"));

    }

    @BeforeEach
    void setup() {
        container.<LoginService>get("lis").logout();
    }

    @Test
    void WhenUserLogsInWithValidCredentialsThenUserIsLoggedIn() {
        // given: user not logged in

        // when: user logs in with valid vredentials
        AutoConsole console = setupLoginBuffer("admin", "admin");
        MainScreen ms = new MainScreen(container, console);
        LoginService lis = container.get("lis");
        Assertions.assertThat(lis.isLoggedIn()).isFalse();
        ms.show();

        // then: user is logged in
        Assertions.assertThat(lis.isLoggedIn()).isTrue();
        Assertions.assertThat(lis.getCurrentUserAlias()).isEqualTo("admin");
    }

    @Test
    void WhenUserLogsInWithInvalidCredentialsThenUserIsNotLoggedIn() {

        AutoConsole console = setupLoginBuffer("admin", "wrong_pw");
        MainScreen ms = new MainScreen(container, console);
        // start flow
        ms.show();
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isFalse();
    }

    @Test
    void WhenUserLogsInWithValidCredentialsButUserIsBlockedThenUserIsNotLoggedIn() {
        // given: user is logged out

        // when: user logs in with credentials of blocked user
        AutoConsole console = setupLoginBuffer("betsy", "betsy");
        MainScreen ms = new MainScreen(container, console);
        ms.show();

        // then: user is not logged in
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isFalse();
    }

    @Test
    void WhenLoggedInUserLogsOutThenUserIsLoggedOut() {

        // given: user is logged in
        LoginService lis = container.get("lis");
        lis.login("rolph", "rolph");
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

}