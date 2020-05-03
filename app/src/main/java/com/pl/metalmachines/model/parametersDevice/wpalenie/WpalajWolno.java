package com.pl.metalmachines.model.parametersDevice.wpalenie;

public class WpalajWolno {

    private String wysokoscWpalania;
    private String stabilnaOdleglosc;

    public WpalajWolno(String wysokoscWpalania, String stabilnaOdleglosc) {
        this.wysokoscWpalania = wysokoscWpalania;
        this.stabilnaOdleglosc = stabilnaOdleglosc;
    }

    public String getWysokoscWpalania() {
        return wysokoscWpalania;
    }

    public void setWysokoscWpalania(String wysokoscWpalania) {
        this.wysokoscWpalania = wysokoscWpalania;
    }

    public String getStabilnaOdleglosc() {
        return stabilnaOdleglosc;
    }

    public void setStabilnaOdleglosc(String stabilnaOdleglosc) {
        this.stabilnaOdleglosc = stabilnaOdleglosc;
    }

    @Override
    public String toString() {
        return "WpalajWolno{" +
                "wysokoscWpalania='" + wysokoscWpalania + '\'' +
                ", stabilnaOdleglosc='" + stabilnaOdleglosc + '\'' +
                '}';
    }
}
