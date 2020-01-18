package com.zbar.kata.coffeemachine.entities;

public enum DrinkType {

    COFFEE("C"),
    CHOCOLATE("H"),
    TEA("T");

    private final String code;

    DrinkType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
