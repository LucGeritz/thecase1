package ninja.pinhole.console2;

import ninja.pinhole.console.AnsiConsole;
import ninja.pinhole.screens.MainScreen;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Test {

    public static void main(String[] args) {
        new Test().run();
    }

    public void run() {
        Container container = setup();

        // temporary hack to avoid having to log in!
        // REMOVE FOR DEMO!!
        // container.<LoginService>get("lis").login("admin", "admin");

        var ac = new AnsiConsole();
        new TestMenu(container, ac).show();
        ac.clear();

    }

    private Container setup() {

        // Initialize the registry
        Container container = new Container();

        // "em" EntityManager
        container.<EntityManager>set("em",
                () -> Persistence
                        .createEntityManagerFactory("MySQL-thecase")
                        .createEntityManager()
        );

        // "lis" LogInServer
        container.<LoginService>set("lis",
                () -> new LoginService(
                        container.get("em")
                )
        );

        return container;
    }

}
