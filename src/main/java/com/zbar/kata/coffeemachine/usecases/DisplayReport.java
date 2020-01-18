package com.zbar.kata.coffeemachine.usecases;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.ports.ReportRepository;

import java.text.DecimalFormat;
import java.util.List;

public class DisplayReport {

    private static final DecimalFormat df = new DecimalFormat("#.##");
    private final ReportRepository repository;

    public DisplayReport(ReportRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        List.of(DrinkType.values()).stream()
                .forEach(this::displayByType);
        System.out.println("Total: " + df.format(repository.sumAll()) + "€");
    }

    private void displayByType(DrinkType type) {
        long res = repository.countByCode(type.getCode());
        System.out.println(type.getCode() + ":" + res);
    }
}
