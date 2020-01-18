package com.zbar.kata.coffeemachine.ports;

import com.zbar.kata.coffeemachine.entities.DrinkType;

public interface ReportRepository {

    void add(DrinkType type);

    long countByCode(String code);

    double sumAll();
}
