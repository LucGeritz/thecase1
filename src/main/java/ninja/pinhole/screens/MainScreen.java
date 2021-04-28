package ninja.pinhole.screens;

import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserIO;
import ninja.pinhole.services.Container;

import java.util.Map;
import java.util.TreeMap;

public class MainScreen extends Screen {

    private final String optionLogin = "1";
    private final String optionAdverts = "2";
    private final String optionUsers = "3";

    private final String optionExit = "x";

    private LoginScreen ls;

    public MainScreen(Container container, UserIO userIO) {
        super(container, "Welkom!", userIO);
        this.options = getOptions();
    }

    private Map<String, Option> getOptions() {
        Map<String, Option> options = new TreeMap<>();
        options.put(optionLogin, new Option(optionLogin, "Log in / Log uit"));
        options.put(optionAdverts, new Option(optionAdverts, "Advertenties"));
        options.put(optionUsers, new Option(optionUsers, "Gebruikers"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    private LoginScreen getLoginScreen() {
        if (this.ls == null) {
            ls = new LoginScreen(container, this.userIO);
        }
        return this.ls;
    }

    @Override
    public void show() {

        // SHOW should
        // call getOption()
        // interpret the chosen option (e.g load a new menu etc)

        boolean show = true;

        while (show) {
            Option picked = this.getOption();

            switch (picked.getId()) {
                case optionExit:
                    show = false;
                    break;
                case optionLogin:
                    launch(getLoginScreen());
                    break;
                case optionAdverts:
                    launch(new AdvertisementScreen(container, this.userIO));
                    break;
                case optionUsers:
                    launch(new UserScreen(container, this.userIO));
                    break;
            }
        }
    }
}
