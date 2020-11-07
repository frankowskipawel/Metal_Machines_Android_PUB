package com.paweldev.maszynypolskie.model.enums;

public enum FileType {
    INNY("Inny", "INNY"),
    PROTOKOL_ODBIORU_TECHNICZNEGO("Protokół odbioru technicznego", "POT"),
    KARTA_GWARANCYJNA("Karta gwarancyjna", "KG"),
    WARUNKI_GWARANCJI("Warunki gwarancji", "WG"),
    PROCEDURA_SERWISOWA("Procedura serwisowa", "PS"),
    PROTOKOL_SZKOLENIA_OPERATOROW("Protokół szkolenia operatorów", "PSO"),
    PROTOKOL_PRZEGLADU_OKRESOWEGO("Protokół przeglądu okresowego", "PPS"),
    PROTOKOL_SERWISOWY("Protokół serwisowy", "PS"),
    PROTOKOL_TECHNICZNY_PRZEDMONTAZOWY("Protokół techniczny przedmontazowy", "PTP"),
    PARAMETERS("Parametry", "PAR");

    String name;
    String alias;

    FileType(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public static FileType getFileTypeAfterName(String name) {
        for (FileType value : FileType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public String getAlias() {
        return alias;
    }
}
