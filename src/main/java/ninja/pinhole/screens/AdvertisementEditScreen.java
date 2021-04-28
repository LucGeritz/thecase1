package ninja.pinhole.screens;

import ninja.pinhole.console.*;
import ninja.pinhole.model.Advertisement;
import ninja.pinhole.model.AdvertisementDao;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class AdvertisementEditScreen extends Screen implements Launchable {

    private Advertisement advertisement;

    private final String optionName = "1";
    private final String optionUser = "2";
    private final String optionDescr = "3";
    private final String optionPrice = "4";
    private final String optionExit = "x";
    private final String optionSave = "s";

    private AdvertisementDao pdao;
    private UserDao udao;
    private EntityManager em;

    public AdvertisementEditScreen(Container container, UserInterface userInterface, Advertisement advertisement) {
        super(container,"Wijzigen advertentie " + advertisement.getId(), userInterface);
        this.advertisement = advertisement;
        options = getOptions();
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
                case optionSave:
                    save(advertisement);
                    break;
                case optionUser:
                    pickUser();
                    break;
                case optionPrice:
                    processPrice();
                    break;
            }
        }

    }

    @Override
    public boolean needsAdminRights() {
        return Launchable.NEEDSADMIN;
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

        var io = new InputOption(optionName, "Naam", userInterface);
        io.setValue(advertisement.getName());
        options.put(optionName, io);

        io = new InputOption(optionDescr, "Omschrijving", userInterface);
        io.setValue(advertisement.getDescription());
        options.put(optionDescr, io);

        io = new InputOption(optionPrice, "Prijs", userInterface);
        io.setValue(advertisement.getPrice().toString());
        options.put(optionPrice, io);

        var o = new Option(optionUser, "Gebruiker");
        o.setValue(advertisement.getUser().getId().toString());
        options.put(optionUser, o);


        options.put(optionSave, new Option(optionSave, "Save"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    private void pickUser() {
        var ep = new EntityPicker<User>(container,"Kies gebruiker",
                userInterface,
                new UserDao(getEntityManager()),
                Launchable.NEEDSLOGIN,
                Launchable.NEEDSADMIN);
        if (launch(ep) && ep.hasPicked()) {
            // update user
            User u = ep.getPicked();
            options.get(optionUser).setValue(u.getId().toString());
        }
    }

    private boolean processPrice() {

        var input = options.get(optionPrice).getValue();
        BigDecimal bd;
        // is it a number?
        try {
            bd = new BigDecimal(options.get(optionPrice).getValue());
        } catch (NumberFormatException nfe) {
            generalMsg = input.concat(" is geen geldig bedrag");
            return false;
        }

        // https://stackoverflow.com/questions/2296110/determine-number-of-decimal-place-using-bigdecimal/4592016
        // Positive with two decimals ?
        if (getNumberOfDecimalPlaces(bd) != 2) {
            generalMsg = "Een geldig bedrag is groter dan nul en heeft twee decimalen";
            return false;
        }

        return true;
    }

    private int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        int scale = bigDecimal.stripTrailingZeros().scale();
        return scale > 0 ? scale : 0;
    }

    private UserDao getUserDao() {
        if (udao == null) {
            udao = new UserDao(getEntityManager());
        }
        return udao;
    }

    private AdvertisementDao getAdvertDao() {
        if (pdao == null) {
            pdao = new AdvertisementDao(getEntityManager());
        }
        return pdao;
    }

    private EntityManager getEntityManager(){
        return em == null ? container.get("em") : em;
    }

    private void save(Advertisement p) {

        if (!this.processPrice()) return;

        p.setName(options.get(optionName).getValue());
        p.setDescription(options.get(optionDescr).getValue());
        p.setPrice(new BigDecimal(options.get(optionPrice).getValue()));

        long userId = Long.parseLong(options.get(optionUser).getValue());
        User u = getUserDao().find(userId);
        p.setUser(u);

        getAdvertDao().update(p);

    }
}
