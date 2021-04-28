package ninja.pinhole.model;

import java.util.Random;

public enum ServiceCategory {
    Clean,
    Repair,
    Consult;

    public static ServiceCategory random(){
        int number = new Random().nextInt(values().length);
        return values()[number];
    }
}
