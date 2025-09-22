package org.examplelongndreou.backendchataiapp.services;


public class Singleton {

    private static Singleton self = null;

    private Singleton() {}

    static public Singleton getInstance() {
        if (self == null) {
            self = new Singleton();
        }
        return self;
    }



}