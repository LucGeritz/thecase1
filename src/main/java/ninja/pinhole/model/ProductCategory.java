package ninja.pinhole.model;

import java.util.Random;

public enum ProductCategory {

    PET("Huisdier"),
    HOBBY("Hobby"),
    CLOTHING("Kleding"),
    FOOD("Voedsel");

    public final String value;

    ProductCategory(String value) {
        this.value = value;
    }

    public static ProductCategory random(){
        int number = new Random().nextInt(values().length);
        return values()[number];
    }
}
