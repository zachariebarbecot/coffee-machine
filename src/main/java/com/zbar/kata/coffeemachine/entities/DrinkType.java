package com.zbar.kata.coffeemachine.entities;

public enum DrinkType {

    COFFEE("C", 0.6),
    CHOCOLATE("H", 0.5),
    TEA("T", 0.4);

    private final String code;
    private final double price;

    DrinkType(String code, double price) {
        this.code = code;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }
}
