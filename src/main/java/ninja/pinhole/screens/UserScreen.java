package ninja.pinhole.screens;

import ninja.pinhole.console.EntityPicker;
import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserIO;
import ninja.pinhole.model.User;
import ninja.pinhole.model.UserDao;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;
import ninja.pinhole.services.LoginService;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.TreeMap;

public class UserScreen extends Screen implements Launchable {

    public final String optionAdd = "1";
    public final String optionEdit = "2";
    public final String optionDelete = "3";
    public final String optionBlock = "4";
    public final String optionExit = "x";

    public UserScreen(Container container, UserIO userIO) {
        super(container,"Gebruikers", userIO);
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
                case optionBlock:
                    blockUser();
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
        options.put(optionBlock, new Option(optionBlock, "Blokkeren"));

        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    /**
     * Let user pick a user, if picked toggle its blocked status
     */
    private void blockUser() {

        String currentAlias = container.<LoginService>get("lis").getCurrentUserAlias();
        EntityManager em = container.get("em");

        // todo use getEntityPicker() Structure
        var ep = new EntityPicker<User>(container, "Blokkeer / Déblokkeer gebruiker",
                userIO,
                new UserDao(em),
                Launchable.NEEDSLOGIN,
                Launchable.NEEDSADMIN);
        ep.setFilter(user -> currentAlias != user.getAlias());

        if (launch(ep) && ep.hasPicked()) {
            User u = ep.getPicked();
            u.setBlocked(!u.isBlocked());
            new UserDao(em).update(u);
        }

    }
}

