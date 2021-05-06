package ninja.pinhole.console2;

import ninja.pinhole.console.UserIO;
import ninja.pinhole.services.Container;

import java.util.Map;
import java.util.TreeMap;

public class LoginMenu extends Menu implements Launchable {

    private final String loginOption = "L";
    private final String logoutOption = "L";
    private final String nameOption = "1";
    private final String pwOption = "2";

    private final String loginKey = "I";
    private final String logoutKey = "O";
    private final String nameKey = "N";
    private final String pwKey = "P";

    public LoginMenu(Container container, UserIO userIO) {
        super(container, userIO, "Login Menu");
    }

    @Override
    public void afterPick() {

        switch (currentOption.getKey()) {
            case "I":
                var x = options.get(nameKey);
                login(options.get(nameOption).getValue(), options.get(pwOption).getValue());
                // Menu changed, reload
                options = defineOptions();
                break;
            case "O":
                // Menu changed, reload
                options = defineOptions();
                break;
        }
    }

    @Override
    protected Map<String, MenuOption> defineOptions() {
        Map<String, MenuOption> options = new TreeMap<>();

        if (lis.isLoggedIn()) {
            // logout option
            options.put(logoutOption, new LogoutOption(logoutKey, "Uitloggen", lis));
        } else {
            // login option
            options.put(loginOption, new MenuOption(loginKey, "Inloggen"));
        }

        options.put(nameOption, new InputOption(nameKey, "Naam", "", userIO));
        options.put(pwOption
                , new InputOption(pwKey, "Wachtwoord", "", userIO));

        options.put("x", new ExitOption());

        return options;
    }

    @Override
    public void launch() {
        show();
    }

    private void login(String name, String pw) {
        lis.login(name, pw);
        if (!lis.isLoggedIn()) {
            lastActionResult = new ActionResult("Inloggen is niet gelukt", MessageType.ERROR);
        }
    }
}
