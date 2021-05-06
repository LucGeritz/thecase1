package ninja.pinhole.console2;

import ninja.pinhole.console.UserIO;
import ninja.pinhole.services.Container;

import java.util.Map;
import java.util.TreeMap;

public class TestMenu extends Menu {

    public TestMenu(Container container, UserIO userIO) {
        super(container, userIO, "Test Menu");
    }

    @Override
    protected Map<String, MenuOption> defineOptions() {
        Map<String, MenuOption> options = new TreeMap<>();

        // Example of an input option
        options.put("1", new InputOption("1", "Greeting", "Hello", userIO)
                .setInputValidation(
                        (s) -> s.length() > 4
                )
        );
        options.put("2", new ToggleOption("2", "Are you happy?", true, userIO).setTrueIndicator("J"));

        options.put("3", new LaunchOption("3","Launch",
                        new PlainLauncher(
                                () -> {
                                    System.out.println("yeah!! launched");
                                })
                )
        );

        options.put("4", new LaunchOption("4", lis.isLoggedIn() ? "Log Out" : "Log In",
                        new PlainLauncher(
                                new LoginMenu(container, userIO)
                        )
                )
        );

        options.put("x", new ExitOption());

        return options;
    }
}
