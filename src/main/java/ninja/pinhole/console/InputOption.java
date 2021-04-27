package ninja.pinhole.console;

/**
 * An InputOption represents an Option which prompts user for input after being picked.
 * Input value is stored in value field.
 */
public class InputOption extends Option {
    private UserInterface userInterface;
    public InputOption(String id, String name, UserInterface userInterface) {
        super(id, name);
        this.userInterface = userInterface;
    }

    @Override
    /**
     * If a InputOption is picked we'll ask the user for an input
     */
    protected void afterPick() {
        userInterface.printInfo("Geef waarde voor " + this.getName() + " ");
        this.setValue(userInterface.get());
    }

}
