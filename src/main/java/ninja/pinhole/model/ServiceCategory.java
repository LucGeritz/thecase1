package ninja.pinhole.model;

import java.util.Random;

public enum ServiceCategory {
    CLEAN("Shoonmaken"),
    REPAIR("Repareren"),
    CONSULT("Advies");

    public final String value;

    ServiceCategory(String value) {
        this.value = value;
    }

    public static ServiceCategory random(){
        int number = new Random().nextInt(values().length);
        return values()[number];
    }
}
