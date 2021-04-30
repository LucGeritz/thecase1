package ninja.pinhole.console;

/**
 * ReadonlyConsole reads values from a buffer, like AutoConsole
 * but any output is ignored.
 */
public class ReadonlyConsole extends AutoConsole {
    @Override
    public void print(String text) {
    }

    @Override
    public void printError(String text) {
    }

    @Override
    public void printInfo(String text) {
    }

    @Override
    public void printWarn(String text) {
    }

    @Override
    public void printTitle(int width, String title, String user) {
    }

    @Override
    public void clear() {
    }

}
