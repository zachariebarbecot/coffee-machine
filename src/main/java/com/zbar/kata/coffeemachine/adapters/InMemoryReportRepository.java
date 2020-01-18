package com.zbar.kata.coffeemachine.adapters;

import com.zbar.kata.coffeemachine.entities.DrinkType;
import com.zbar.kata.coffeemachine.ports.ReportRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryReportRepository implements ReportRepository {

    private List<DrinkType> commands = new ArrayList<>();

    @Override
    public void add(DrinkType type) {
        this.commands.add(type);
    }

    @Override
    public long countByCode(String code) {
        return this.commands.stream()
                .filter(type -> type.getCode().equals(code))
                .count();
    }

    @Override
    public double sumAll() {
        return this.commands.stream()
                .mapToDouble(type -> type.getPrice())
                .sum();
    }
}
