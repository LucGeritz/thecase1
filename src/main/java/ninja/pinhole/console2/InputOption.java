package ninja.pinhole.console2;

import ninja.pinhole.console.UserIO;

public class InputOption extends MenuOption {

    private UserIO userIO;
    private InputValidator validator;

    public InputOption(String key, String text, String value, UserIO userIO) {
        super(key, text, value);
        this.userIO = userIO;

        // default validator always return true, can be overridden by setInputValidator()
        this.validator = (String input) -> true;
    }

    @Override
    public ActionResult doAction() {

        userIO.printInfo("Geef waarde voor " + this.text + " ");
        boolean wait = true;

        while(wait) {
            this.setValue(userIO.get());
            wait = !validator.validate(value);
            if(wait){
                userIO.printError("Ongeldige waarde");
            }
        }

        return new ActionResult();
    }

    @Override
    public String toString() {
        return text + " [" + value + "]";
    }

    public InputOption setInputValidation(InputValidator validator){
        this.validator = validator;
        return this;
    }

}
