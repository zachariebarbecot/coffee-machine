package com.zbar.kata.coffeemachine.ports;

public interface EmailNotifierPort {

    void notifyMissingDrink(String drink);
}
