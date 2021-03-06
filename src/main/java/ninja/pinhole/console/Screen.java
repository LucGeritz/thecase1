package ninja.pinhole.console;

import ninja.pinhole.services.Container;
import ninja.pinhole.services.Launchable;
import ninja.pinhole.services.Launcher;
import ninja.pinhole.services.LoginService;

import java.util.Map;
import java.util.TreeMap;

public abstract class Screen {

    protected UserInterface userInterface;
    protected final int screenWidth = 40;
    protected String generalMsg;
    protected Map<String, Option> options = new TreeMap<>();
    private String title;

    protected Container container;
    private LoginService lis;

    public Screen(Container container, String title, UserInterface userInterface) {
        this.generalMsg = "";
        this.title = title;
        this.userInterface = userInterface;
        this.container = container;
        this.lis = container.get("lis");
    }

    public Option getOption() {
        String line;

        do {
            this.displayMenu();
            line = userInterface.get();
            if (this.validate(line)) {
                break;
            }

        } while (true);

        Option option = this.options.get(line);
        // Do any after pick processing
        option.afterPick();

        return option;
    }

    /**
     * Display numbered options with optional message and prompt.
     */
    private void displayMenu() {

        // printer.clear();

        userInterface.printTitle(this.screenWidth, this.title,
                lis.isLoggedIn() ? lis.getCurrentUserAlias() : "gast");

        for (var menuEntry : this.options.entrySet()) {
            userInterface.print(menuEntry.getValue().toString());
            menuEntry.getValue().resetMessage();
        }

        userInterface.printError(this.generalMsg);
        userInterface.printInfo("Geef keuze in");

        this.generalMsg = "";

    }

    private boolean validate(String line) {

        if (!this.options.containsKey(line)) {
            this.generalMsg = "Ongeldige optie";
            return false;
        }
        return true;

    }

    /**
     * Launch a program and set its result as generalMsg
     * @return true if no message set
     */
    protected boolean launch(Launchable program){
        var launcher = new Launcher(container.get("lis"));
        launcher.launch(program);
        generalMsg = launcher.getErrorMsg();
        return generalMsg.equals("");
    }

    /**
     * Show the screen and act on chosen option
     */
    public abstract void show();
}
