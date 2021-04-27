package ninja.pinhole;

import ninja.pinhole.console.AnsiConsole;
import ninja.pinhole.screens.MainScreen;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Marktplaats {

    public static void main(String[] args) {
        new Marktplaats().run();
    }

    public void run() {
        Container container = setup();

        // temporary hack to avoid having to log in!
        container.<LoginService>get("lis").login("admin", "admin");

        new MainScreen(container, new AnsiConsole()).show();
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
