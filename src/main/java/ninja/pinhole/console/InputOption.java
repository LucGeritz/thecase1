package ninja.pinhole.console;

/**
 * An InputOption represents an Option which prompts user for input after being picked.
 * Input value is stored in value field.
 */
public class InputOption extends Option {
    private UserIO userIO;
    public InputOption(String id, String name, UserIO userIO) {
        super(id, name);
        this.userIO = userIO;
    }

    @Override
    /**
     * If a InputOption is picked we'll ask the user for an input
     */
    protected void afterPick() {
        userIO.printInfo("Geef waarde voor " + this.getName() + " ");
        this.setValue(userIO.get());
    }

}
