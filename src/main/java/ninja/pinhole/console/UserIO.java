package ninja.pinhole.console;

public interface UserIO {
    // Print text as "standard"
    void print(String text);
    // Print text as "error"
    void printError(String text);
    // Print text as "info"
    void printInfo(String text);
    // Print text as "warning"
    void printWarn(String text);
    // Print a title
    void printTitle(int width, String title, String user);
    // Clear the screen
    void clear();
    // get string from user
    String get();
}
