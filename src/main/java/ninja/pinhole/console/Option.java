package ninja.pinhole.console;

/**
 * Option represents a single menu option with optional message appended.
 */
public class Option {

    private String value;
    private String id;
    private String name;
    private String message;
    private boolean secret = false;

    protected Option(String id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.setValue(""); // add to constructor
    }

    public Option(String id, String name) {
        this(id, name, "");
    }

    public void resetMessage() {
        this.message = "";
    }

    /**
     * If secret is true the value of the option is shown as ***
     */
    public Option setSecret() {
        this.secret = true;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String line = this.getId() + " " + this.getName();

        String value = this.getValue();

        if (!this.getValue().isEmpty()) {
            line = line.concat(" [" + (this.secret ? "*".repeat(value.length()) : value) + "]");
        }

        if (!this.getMessage().isEmpty()) {
            line = line.concat(" " + this.getMessage());
        }

        return line;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Default implementation of afterPick does nothing.
     * Descendants can override this
     */
    protected void afterPick() {}

}


