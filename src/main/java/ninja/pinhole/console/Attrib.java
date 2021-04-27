package ninja.pinhole.console;

// ESC is \033
public enum Attrib {
    FGBLACK("[30m"),
    FGRED("\033[31m"),
    BGRED("\033[41m"),
    FGGREEN("\033[32m"),
    FGYELLOW("\033[33m"),
    FGBLUE("\033[34m"),
    FGMAGENTA("[35m"),
    FGCYAN("\033[36m"),
    BGCYAN("\033[46m"),
    FGWHITE("\033[97m"),
    BGWHITE("\033[107m"),
    BOLD("[1m"),
    DEFAULT("\033[0m");

    public final String value;

    Attrib(String value) {
        this.value = value;
    }

}
