package ninja.pinhole.screens;

import ninja.pinhole.console.AutoConsole;
import ninja.pinhole.console.ReadonlyConsole;
import ninja.pinhole.model.Advertisement;
import ninja.pinhole.model.AdvertisementDao;
import ninja.pinhole.model.Product;
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

class EditAdvertE2E {

    private static final int initialReccount = 10;
    private static Container container;
    private static final int waitTime = 0; // milliseconds
    private static Map<String, Long> name2Id = new HashMap<>();

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
        new AdvertisementDao(container.get("em")).findAll().forEach(adv -> name2Id.put(adv.getName(), adv.getId()));

    }

    @BeforeEach
    void setup() {
        // Always logged in as admin
        container.<LoginService>get("lis").login("admin", "admin");
    }

    @Test
    void WhenProductIsChangedThenValuesShouldBeSavedInDb() {

        String newName = "Stoomtrein";
        long rolph = new UserDao(container.get("em")).findByAlias("rolph").getId();
        String newCat = "3";
        String newPrice = "100.01";
        String newDescr = "Test";

        // Must be logged in!
        Assertions.assertThat(container.<LoginService>get("lis").isLoggedIn()).isTrue();

        var console = setPathToEditAdvertScreen(new ReadonlyConsole());

        // remember id oma fiets (product)
        long id = name2Id.get("Oma fiets").longValue();

        console.buffer(id + "") // pick oma fiets
                .buffer("1") // pick field "naam"
                .buffer(newName) // new value for name
                .buffer("2") // pick field "gebruiker"
                .buffer(rolph + "") // new value for gebruiker
                .buffer("3") // pick field (product)categorie
                .buffer(newCat) // pick food cat
                .buffer("4") // pick field "omschrijving"
                .buffer(newDescr) // new value for omscgrijving
                .buffer("5") // pick field "prijs"
                .buffer(newPrice) // new value for prijs
                .buffer("s") // save
                .buffer("x") // exit advert-edit screen
                .buffer("x") // exit advert screen
                .buffer("x"); // exit main screen

        // go!
        new MainScreen(container, console).show();

        // Read again
        var advert = getAdvert(id);
        var user = advert.getUser();
        Assertions.assertThat(advert.getName()).isEqualTo(newName);
        Assertions.assertThat(advert.getDescription()).isEqualTo(newDescr);
        Assertions.assertThat(advert.getPrice() + "").isEqualTo(newPrice);
        Assertions.assertThat(((Product) advert).getCategory()).isEqualTo("Food");
        Assertions.assertThat(user.getAlias()).isEqualTo("rolph");
    }

    private AutoConsole  setPathToEditAdvertScreen(AutoConsole console ) {
        console.buffer("2") // pick advert screen
                .buffer("2") // pick edit screen
                .setWait(waitTime);

        return console;
    }

    private Advertisement getAdvert(long id) {
        return new AdvertisementDao(container.get("em")).find(id);
    }

}