package com.zbar.kata.coffeemachine.usecases;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.ports.ReportRepository;

import java.text.DecimalFormat;
import java.util.List;

public class DisplayReport {

    private final ReportRepository repository;
    private static DecimalFormat df = new DecimalFormat("#.##");

    public DisplayReport(ReportRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        List.of(DrinkType.values()).stream()
                .forEach(type -> {
                    long res = repository.countByCode(type.getCode());
                    System.out.println(type.getCode() + ":" + res);
                });
        System.out.println("Total: " + df.format(repository.sumAll()) + "€");
    }
}
