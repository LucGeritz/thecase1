package ninja.pinhole.model;

import ninja.pinhole.console.Pickable;

import java.util.Random;

public enum ProductCategory  {

    Pet,
    Hobby,
    Clothing,
    Food;

    public static ProductCategory random(){
        int number = new Random().nextInt(values().length);
        return values()[number];
    }

 }
