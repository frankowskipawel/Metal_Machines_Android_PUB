package com.pl.metalmachines.model.parametersDevice.wpalenie;

public class WpalajKolem {

    private String predkosc;

    public WpalajKolem(String predkosc) {
        this.predkosc = predkosc;
    }

    public String getPredkosc() {
        return predkosc;
    }

    public void setPredkosc(String predkosc) {
        this.predkosc = predkosc;
    }

    @Override
    public String toString() {
        return "WpalajKolem{" +
                "predkosc='" + predkosc + '\'' +
                '}';
    }
}
