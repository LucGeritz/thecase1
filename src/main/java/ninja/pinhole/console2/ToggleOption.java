package ninja.pinhole.console2;

import ninja.pinhole.console.UserIO;

public class ToggleOption extends MenuOption {

    private UserIO userIO;
    private boolean toggle;
    private String trueIndicator = "X";
    private String falseIndicator = " ";

    public ToggleOption(String key, String text, boolean toggle, UserIO userIO) {
        // value is totally ignored in this option!
        super(key, text, "");
        this.userIO = userIO;
        this.toggle = toggle;
    }

    @Override
    public ActionResult doAction() {
        toggle = !toggle;
        return new ActionResult();
    }

    @Override
    public String toString() {
        return "[" + (toggle ? trueIndicator : falseIndicator) + "] " + text;
    }

    public boolean toBoolean() {
        return toggle;
    }

    public ToggleOption setTrueIndicator(String indicator) {
        trueIndicator = indicator;
        return this;
    }

    public ToggleOption setFalseIndicator(String indicator) {
        falseIndicator = indicator;
        return this;
    }
}
