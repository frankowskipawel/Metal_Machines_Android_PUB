package com.pl.metalmachines.model.parametersDevice.ciecie;

public class StopWolno {

    private String dlugosc;
    private String predkosc;

    public StopWolno(String dlugosc, String predkosc) {
        this.dlugosc = dlugosc;
        this.predkosc = predkosc;
    }

    public String getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(String dlugosc) {
        this.dlugosc = dlugosc;
    }

    public String getPredkosc() {
        return predkosc;
    }

    public void setPredkosc(String predkosc) {
        this.predkosc = predkosc;
    }
}
