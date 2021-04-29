package ninja.pinhole.console;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoConsole differs from AnsiConsole only in the way it gets its input.
 * It uses a string buffer which can be filled by the caller beforehand.
 * It is meant for use in E2E-tests.
 */
public class AutoConsole extends AnsiConsole {

    private List<String> buf = new ArrayList<>();
    private int pointer = 0;
    private int waitTime;

    /**
     * Add a String entry to the input buffer
     */
    public AutoConsole buffer(String input) {
        buf.add(input);
        return this;
    }

    /**
     * Time to wait in ms after input is shown on screen
     */
    public void setWait(int ms) {
        this.waitTime = ms;
    }

    @Override
    public String get() {

        if (pointer > (buf.size() - 1) || buf.size() == 0) {
            throw new RuntimeException("Empty Buffer");
        }
        // Read input from buffer and display it
        String input = buf.get(pointer++);
        print(input);

        if (waitTime > 0) {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return input;
    }
}
