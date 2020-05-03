package com.pl.metalmachines.model.parametersDevice.enums;

public enum EnumWpalenie {
    NAZWA_WPALENIE("Wpalenie"),
    PREDKOSC_CIECIA("Prędkośc Cięcia","m/min"),
    CYKL_PRACY("Cykl pracy","%"),
    CZESTOTLIWOSC("Częstotliwość","Hz");

    String name;
    String unit;

    EnumWpalenie(String name) {
        this.name = name;
    }

    EnumWpalenie(String name, String unit) {
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
