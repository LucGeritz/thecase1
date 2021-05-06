package ninja.pinhole.console2;

/**
 * Any option can be made into an exit option with setExit(true)
 * This is just a handy option which always set exit to true
 */
public class ExitOption extends MenuOption{

    public ExitOption() {
        this("Exit");
    }

    public ExitOption(String text){
        super("x",text, "");
        exit = true;
    }

}
