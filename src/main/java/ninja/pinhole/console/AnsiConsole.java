package ninja.pinhole.console;

import java.util.Scanner;

/**
 * AnsiConsole is a UserIO implementation for a Ansi-compatible console
 */
public class AnsiConsole implements UserIO {

    @Override
    public void print(String text) {
        printString(Attrib.DEFAULT, text);
    }

    @Override
    public void printError(String text) {
        printString(Attrib.FGRED, text);
    }

    @Override
    public void printInfo(String text) {
        printString(Attrib.FGGREEN, text);
    }

    @Override
    public void printWarn(String text) {
        printString(Attrib.FGYELLOW, text);
    }

    @Override
    public void printTitle(int width, String title, String user) {
        user = "│ ".concat(user);
        int restWidth = width - title.length() - user.length() - 2; // -2 for margins of 1 space
        printString(new Attrib[]{Attrib.FGWHITE, Attrib.BGCYAN, Attrib.BOLD},
                " " + title + " ".repeat(restWidth) + user + " ");
    }

    @Override
    public void clear() {
        // Nothing seems to work, known compatibility problem ANSI-W8
        // @see https://stackoverflow.com/questions/2979383/java-clear-the-console
        // so I'll just use this very dirty hack, print a lot of backspaces..
        for (int i = 0; i < 10000; i++) {
            System.out.println("\b");
        }
        // And cursor-home doesn't work either
    }

    @Override
    public String get() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    private void printString(Attrib attrib, String text) {
        System.out.println(attrib.value + text + Attrib.DEFAULT.value);
    }

    private void printString(Attrib[] attribs, String text) {
        String prefix = "";

        for (Attrib attrib : attribs) {
            prefix += attrib.value;
        }

        System.out.println(prefix + text + Attrib.DEFAULT.value);
    }


}
