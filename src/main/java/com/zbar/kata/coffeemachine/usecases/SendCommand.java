package com.zbar.kata.coffeemachine.usecases;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;

import java.util.StringJoiner;

public class SendCommand {

    private final DrinkMakerPort maker;

    public SendCommand(DrinkMakerPort maker) {
        this.maker = maker;
    }

    public void execute(DrinkType type) {
        var stringify = stringifyCommand(type, 0);
        this.maker.make(stringify);
    }

    public void execute(DrinkType type, int numberOfSugars) {
        if (isThereTooMuchSugars(numberOfSugars)) {
            throw new TooMuchSugarsException();
        }
        var stringify = stringifyCommand(type, numberOfSugars);
        this.maker.make(stringify);
    }

    private boolean isThereTooMuchSugars(int numberOfSugars) {
        return numberOfSugars > 2;
    }

    private String stringifyCommand(DrinkType type, int numberOfSugars) {
        var joiner = new StringJoiner(":");
        joiner.add(type.getCode());
        if (numberOfSugars > 0) {
            joiner.add(Integer.toString(numberOfSugars));
            joiner.add("0");
        } else {
            joiner.add(":");
        }
        return joiner.toString();
    }
}
