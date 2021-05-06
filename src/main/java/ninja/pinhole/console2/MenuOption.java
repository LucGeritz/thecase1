package ninja.pinhole.console2;

/**
 * MenuOption is the base class for all menu options
 * It does have a value but it cannot be edited
 * Its doAction() does nothing.
 * Still you can use it. E.g. if the option is completely handled
 * by a Menu SubClass
 */
public class MenuOption {

    protected String text;
    protected String value;
    private String key;

    protected boolean exit;

    public MenuOption(String key, String text, String value) {
        this.text = text;
        this.value = value;
        this.key = key;
    }

    public MenuOption(String key, String text)
    {
        this(key, text, "");
    }

    public MenuOption setExit(boolean exit) {
        this.exit = exit;
        return this;
    }

    public MenuOption setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean getExit() {
        return this.exit;
    }

    public String getText() {
        return this.text;
    }

    @Override
    /**
     * Default way showing an option
     * It shows text and value
     */
    public String toString() {
        return text + " " + value;
    }

    public ActionResult doAction(){
        return new ActionResult();
    };

    protected String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
