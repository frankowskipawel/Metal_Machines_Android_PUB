package com.pl.metalmachines.model.enums;

public enum ServiceType {
    INSTALACJA ("INSTALACJA"),
    SZKOLENIE_OPERATOROW ("SZKOLENIE OPARATORÓW"),
    PRZEGLAD_OKRESOWY ("PRZEGLĄD OKRESOWY"),
    NAPRAWA ("NAPRAWA");

    String name;

    ServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
