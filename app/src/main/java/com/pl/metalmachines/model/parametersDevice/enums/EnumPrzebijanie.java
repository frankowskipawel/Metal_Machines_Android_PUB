package com.pl.metalmachines.model.parametersDevice.enums;

public enum EnumPrzebijanie {
    NAZWA_PRZEBIJANIE("Przebijanie"),
    CZAS_KROKU ("Czas kroku","ms"),
    WYSOKOSC_PRZEBIJANIA ("Wysokość przebijania","mm"),
    GAZ_PRZEBIJANIA ("Gaz przebijania",null),
    CISNIENIE_PRZEBIJANIA ("Ciśnienie przebijania","BAR"),
    NATEZENIE_PRZEBIJANIA ("Natężenie przebijania","%"),
    NATEZENIE_PRZEBIJANIA2 ("Natężenie przebijania","W"),
    MOC_PRZEBIJANIA ("Moc przebijania","%"),
    CZESTOTLIWOSC_PRZEBIJANIA ("Częstotliwość przebijania","Hz"),
    SZEROKOSC_WIAZKI ("Szerokość wiązki","x"),
    FOCUS_PRZEBIJANIA ("Focus przebijania","mm"),
    CZAS_PRZEBIJANIA ("Czas przebijania","ms"),
    PRZEDMUCH ("Przedmuch","ms");


    String name;
    String unit;

    EnumPrzebijanie(String name) {
        this.name = name;
    }

    EnumPrzebijanie(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
