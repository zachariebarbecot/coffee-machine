package com.zbar.kata.coffeemachine.usecases;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.NotEnoughMoneyException;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class SendCommand {

    private final DrinkMakerPort maker;
    private static DecimalFormat df = new DecimalFormat("#.##");

    public SendCommand(DrinkMakerPort maker) {
        this.maker = maker;
    }

    public void execute(DrinkType type, double money) {
        checkIfEnoughMoney(type, money);
        stringifyAndSendCommand(type, 0);
    }

    public void execute(DrinkType type, double money, int numberOfSugars) {
        checkIfEnoughMoney(type, money);
        checkIfTooMuchSugarsAsked(numberOfSugars);
        stringifyAndSendCommand(type, numberOfSugars);
    }

    private void checkIfEnoughMoney(DrinkType type, double money) {
        if (type.getPrice() > money) {
            var stringify = stringifyMessage(df.format(type.getPrice() - money) + "€ missing");
            this.maker.make(stringify);
            throw new NotEnoughMoneyException();
        }
    }

    private void checkIfTooMuchSugarsAsked(int numberOfSugars) {
        if (isThereTooMuchSugars(numberOfSugars)) {
            throw new TooMuchSugarsException();
        }
    }

    private boolean isThereTooMuchSugars(int numberOfSugars) {
        return numberOfSugars > 2;
    }

    private void stringifyAndSendCommand(DrinkType type, int numberOfSugars) {
        var stringify = stringifyCommand(type, numberOfSugars);
        this.maker.make(stringify);
    }

    private String stringifyCommand(DrinkType type, int numberOfSugars) {
        var joiner = new StringJoiner(":");
        joiner.add(type.getCode());
        if (hasSugars(numberOfSugars)) {
            joiner.add(Integer.toString(numberOfSugars));
            joiner.add("0");
        } else {
            joiner.add(":");
        }
        return joiner.toString();
    }

    private boolean hasSugars(int numberOfSugars) {
        return numberOfSugars > 0;
    }

    private String stringifyMessage(String message) {
        return "M:" + message;
    }
}
