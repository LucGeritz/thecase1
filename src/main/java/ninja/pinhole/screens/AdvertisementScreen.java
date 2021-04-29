package ninja.pinhole.screens;

import ninja.pinhole.console.EntityPicker;
import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserIO;
import ninja.pinhole.model.Advertisement;
import ninja.pinhole.model.AdvertisementDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.TreeMap;

public class AdvertisementScreen extends Screen implements Launchable {

    public final String optionAdd = "1";
    public final String optionEdit = "2";
    public final String optionDelete = "3";
    public final String optionExit = "x";

    public AdvertisementScreen(Container container, UserIO userIO) {
        super(container, "Advertenties", userIO);
        this.options = getOptions();
    }

    @Override
    public void show() {

        boolean show = true;

        while (show) {
            Option picked = this.getOption();

            switch (picked.getId()) {
                case optionExit:
                    show = false;
                    break;
                case optionAdd:
                    generalMsg = "Nog niet geïmplementeerd";
                    break;
                case optionDelete:
                    generalMsg = "Nog niet geïmplementeerd";
                    break;
                case optionEdit:
                    editAdvert();
                    break;
            }
        }
    }


    @Override
    public boolean needsAdminRights() {
        return Launchable.NEEDSNOADMIN;
    }

    @Override
    public boolean needsLogIn() {
        return Launchable.NEEDSLOGIN;
    }

    @Override
    public void launch() {
        show();
    }

    private Map<String, Option> getOptions() {
        Map<String, Option> options = new TreeMap<>();
        options.put(optionAdd, new Option(optionAdd, "Toevoegen"));
        options.put(optionEdit, new Option(optionEdit, "Wijzigen"));
        options.put(optionDelete, new Option(optionDelete, "Verwijderen"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    /**
     * Let user pick a advert, if picked launch advert edit screen
     */
    private void editAdvert() {

//        var ep = new EntityPicker<Advertisement>(container,"Kies advertentie",
//                userIO,
//                new AdvertisementDao(container.<EntityManager>get("em")),
//                Launchable.NEEDSLOGIN,
//                Launchable.NEEDSADMIN);

        var ep = new EntityPicker.Builder<Advertisement>(container)
                .withUserIO(userIO)
                .withTitle("Kies advertentie")
                .withDao(new AdvertisementDao(container.<EntityManager>get("em")))
                .needsAdmin(Launchable.NEEDSADMIN)
                .needsLogin(Launchable.NEEDSLOGIN)
                .build();

        if (launch(ep) && ep.hasPicked()) {
            launch(new AdvertisementEditScreen(container, userIO, (Advertisement) ep.getPicked()));
        }
    }
}

