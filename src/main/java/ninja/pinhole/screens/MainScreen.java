package ninja.pinhole.screens;

import ninja.pinhole.console.Option;
import ninja.pinhole.console.Screen;
import ninja.pinhole.console.UserInterface;
import ninja.pinhole.services.Container;

import java.util.Map;
import java.util.TreeMap;

public class MainScreen extends Screen {

    private final String optionLogin = "1";
    private final String optionProducts = "2";
    private final String optionUsers = "3";
    private final String optionExit = "x";

    private LoginScreen ls;

    public MainScreen(Container container, UserInterface userInterface) {
        super(container, "Welkom!", userInterface);
        this.options = getOptions();
    }

    private Map<String, Option> getOptions() {
        Map<String, Option> options = new TreeMap<>();
        options.put(optionLogin, new Option(optionLogin, "Log in / Log uit"));
        options.put(optionProducts, new Option(optionProducts, "Artikelen"));
        options.put(optionUsers, new Option(optionUsers, "Gebruikers"));
        options.put(optionExit, new Option(optionExit, "Exit"));

        return options;
    }

    private LoginScreen getLoginScreen() {
        if (this.ls == null) {
            ls = new LoginScreen(container, this.userInterface);
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
                case optionProducts:
                    launch(new ProductScreen(container, this.userInterface));
                    break;
                case optionUsers:
                    launch(new UserScreen(container, this.userInterface));
                    break;
            }
        }
    }
}
