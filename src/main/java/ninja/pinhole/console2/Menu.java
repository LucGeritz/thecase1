package ninja.pinhole.console2;

import ninja.pinhole.console.UserIO;
import ninja.pinhole.services.Container;
import ninja.pinhole.services.LoginService;

import java.util.Map;

public abstract class Menu {

    protected Container container;
    protected LoginService lis;
    protected MenuOption currentOption;
    protected UserIO userIO;
    protected ActionResult lastActionResult;

    protected Map<String, MenuOption> options;
    private final int screenWidth = 70;
    private final String guestName = "gast";
    private final String title;


    public Menu(Container container, UserIO userIO, String title) {
        this.userIO = userIO;
        this.container = container;
        this.title = title;
        // always need login service so get it now
        this.lis = container.get("lis");
        this.userIO = userIO;
    }

    public void show() {



        boolean show = true;

        while (show) {


            if (options == null) {
                options = defineOptions();
            }

            currentOption = getOption();

            // empty any messages since we'll start interpreting input again
            lastActionResult = new ActionResult();

            if (currentOption.getExit()) {
                // indicates stop
                show = false;
            } else {
                currentOption.doAction();
            }

            afterPick();

        }

    }

    protected void afterPick(){

    }

    protected abstract Map<String, MenuOption> defineOptions();

    private void displayMenu() {

        userIO.clear();
        userIO.printTitle(this.screenWidth, this.title,
                lis.isLoggedIn() ? lis.getCurrentUserAlias() : guestName);

        for (var menuEntry : this.options.entrySet()) {
            String line = menuEntry.getKey() + " ";
            line += menuEntry.getValue().toString() + " ";
            userIO.print(line);
        }

        switch (lastActionResult.type) {
            case INFO -> userIO.printInfo(lastActionResult.message);
            case WARNING -> userIO.printWarn(lastActionResult.message);
            case ERROR -> userIO.printError(lastActionResult.message);
        }

        userIO.printInfo("Geef keuze in");


    }

    protected MenuOption getOption() {
        String line;

        do {
            this.displayMenu();
            line = userIO.get();
            if (this.validate(line)) {
                break;
            }

        } while (true);

        return this.options.get(line);
    }

    private boolean validate(String line) {

        if (!this.options.containsKey(line)) {
            lastActionResult = new ActionResult("Ongeldige keuze", MessageType.WARNING);
            return false;
        }
        return true;
    }

}
