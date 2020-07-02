package com.hw.weather;


public class Singleton {

    private static Singleton singleton = null;
    private String city = null;

    private Singleton(){
    }

    public String getString() throws  NullPointerException {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static Singleton getSingleton() {
            return singleton = singleton == null ? new Singleton() : singleton;
    }
}
