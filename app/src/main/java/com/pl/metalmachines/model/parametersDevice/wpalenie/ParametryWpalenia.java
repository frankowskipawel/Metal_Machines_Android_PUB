package com.pl.metalmachines.model.parametersDevice.wpalenie;

public class ParametryWpalenia {

    private String predkoscCiecia;
    private String cyklPracy;
    private String czestotliwosc;

    public ParametryWpalenia(String predkoscCiecia, String cyklPracy, String czestotliwosc) {
        this.predkoscCiecia = predkoscCiecia;
        this.cyklPracy = cyklPracy;
        this.czestotliwosc = czestotliwosc;
    }

    public String getPredkoscCiecia() {
        return predkoscCiecia;
    }

    public void setPredkoscCiecia(String predkoscCiecia) {
        this.predkoscCiecia = predkoscCiecia;
    }

    public String getCyklPracy() {
        return cyklPracy;
    }

    public void setCyklPracy(String cyklPracy) {
        this.cyklPracy = cyklPracy;
    }

    public String getCzestotliwosc() {
        return czestotliwosc;
    }

    public void setCzestotliwosc(String czestotliwosc) {
        this.czestotliwosc = czestotliwosc;
    }

    @Override
    public String toString() {
        return "ParametryWpalenia{" +
                "predkoscCiecia='" + predkoscCiecia + '\'' +
                ", cyklPracy='" + cyklPracy + '\'' +
                ", czestotliwosc='" + czestotliwosc + '\'' +
                '}';
    }
}
