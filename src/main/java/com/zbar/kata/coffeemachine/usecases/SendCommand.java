package com.zbar.kata.coffeemachine.usecases;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.entities.NotEnoughMoneyException;
import com.zbar.kata.coffeemachine.entities.RunningOutException;
import com.zbar.kata.coffeemachine.entities.TooMuchSugarsException;
import com.zbar.kata.coffeemachine.ports.BeverageQuantityCheckerPort;
import com.zbar.kata.coffeemachine.ports.DrinkMakerPort;
import com.zbar.kata.coffeemachine.ports.EmailNotifierPort;
import com.zbar.kata.coffeemachine.ports.ReportRepository;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class SendCommand {

    private final DrinkMakerPort maker;
    private final ReportRepository repository;
    private final BeverageQuantityCheckerPort checker;
    private final EmailNotifierPort notifier;
    private static DecimalFormat df = new DecimalFormat("#.##");

    public SendCommand(DrinkMakerPort maker, ReportRepository repository, BeverageQuantityCheckerPort checker, EmailNotifierPort notifier) {
        this.maker = maker;
        this.repository = repository;
        this.checker = checker;
        this.notifier = notifier;
    }

    public void execute(DrinkType type, double money, int numberOfSugars, boolean extraHot) {
        checkIfEnoughMoney(type, money);
        checkIfTooMuchSugarsAsked(numberOfSugars);
        checkIfBeverageQuantityIsGood(type);
        repository.add(type);
        stringifyAndSendCommand(type, numberOfSugars, extraHot);
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

    private void checkIfBeverageQuantityIsGood(DrinkType type) {
        if (checker.isEmpty(type.getCode())) {
            notifier.notifyMissingDrink(type.getCode());
            var stringify = stringifyMessage("Running out of " + type.getCode());
            this.maker.make(stringify);
            throw new RunningOutException();
        }
    }

    private boolean isThereTooMuchSugars(int numberOfSugars) {
        return numberOfSugars > 2;
    }

    private void stringifyAndSendCommand(DrinkType type, int numberOfSugars, boolean extraHot) {
        var stringify = stringifyCommand(type, numberOfSugars, extraHot);
        this.maker.make(stringify);
    }

    private String stringifyCommand(DrinkType type, int numberOfSugars, boolean extraHot) {
        var joiner = new StringJoiner(":");
        joiner.add(composeTypeCode(type, extraHot));
        if (hasSugars(numberOfSugars)) {
            joiner.add(Integer.toString(numberOfSugars));
            joiner.add("0");
        } else {
            joiner.add(":");
        }
        return joiner.toString();
    }

    private String composeTypeCode(DrinkType type, boolean extraHot) {
        if (DrinkType.ORANGE_JUICE.equals(type)) {
            return type.getCode();
        }
        return extraHot ? type.getCode() + "h" : type.getCode();
    }

    private boolean hasSugars(int numberOfSugars) {
        return numberOfSugars > 0;
    }

    private String stringifyMessage(String message) {
        return "M:" + message;
    }
}
