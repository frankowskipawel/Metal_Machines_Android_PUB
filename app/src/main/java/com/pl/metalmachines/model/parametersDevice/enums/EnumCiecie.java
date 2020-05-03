package com.pl.metalmachines.model.parametersDevice.enums;

public enum EnumCiecie {
    NAZWA_CIECIE("Cięcie"),
    PREDKOSC_CIECIA("Prędkość cięcia", "m/min"),
    WYSOKOSC_PODNOSZENIA("Wysokość Podnoszenia","mm"),
    WYSOKOSC_CIECIA("Wysokość cięcia","mm"),
    GAZ_CIECIA("Gaz cięcia",null),
    CISNIENIE_CIECIA("Ciśnienie cięcia","BAR"),
    NATEZENIE_CIECIA("Natężenie cięcia","%"),
    NATEZENIE_CIECIA2("Natężenie cięcia","W"),
    MOC_CIECIA("Moc cięcia","%"),
    CZESTOTLIWOSC_PRZEBIEGU("Częstotliwość przebiegu","Hz"),
    SZEROKOSC_WIAZKI("Szerokość wiązki", "x"),
    FOCUS("Focus","mm"),
    CZAS_ZWLOKI("Czas zwłoki","ms"),
    OPOZNIENIE_WLACZENIA_WIAZKI("Opóźnienie włączenia wiązki","ms");


    String name;
    String unit;

    EnumCiecie(String name) {
        this.name = name;
    }

    EnumCiecie(String name, String unit) {
        this(name);
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
