package ninja.pinhole.console;

import java.util.ArrayList;
import java.util.List;

public class AutoConsole extends AnsiConsole {

    List<String> buf = new ArrayList<>();
    int pointer = 0;
    int waitTime;

    public AutoConsole buffer(String input) {
        buf.add(input);
        return this;
    }

    public void setWait(int ms) {
        this.waitTime = ms;
    }

    @Override
    public String get() {
        if (pointer > (buf.size() - 1) || buf.size() == 0) {
            throw new RuntimeException("Empty Buffer");
        }
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
