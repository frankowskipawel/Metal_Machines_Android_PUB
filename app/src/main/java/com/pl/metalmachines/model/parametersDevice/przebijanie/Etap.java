package com.pl.metalmachines.model.parametersDevice.przebijanie;

public class Etap {

    private String czasKroku;
    private String wysokoscPrzebijania;
    private String gazPrzebijania;
    private String cisnieniePrzebijania;
    private String natezeniePrzebieguProcent;
    private String mocPrzebijania;
    private String czestotliwoscPrzebijania;
    private String szerokoscWiazki;
    private String focusPrzebijania;
    private String czasPrzebijania;
    private String przedmuch;

    public Etap(String czasKroku, String wysokoscPrzebijania, String gazPrzebijania, String cisnieniePrzebijania, String natezeniePrzebieguProcent, String mocPrzebijania, String czestotliwoscPrzebijania, String szerokoscWiazki, String focusPrzebijania, String czasPrzebijania, String przedmuch) {
        this.czasKroku = czasKroku;
        this.wysokoscPrzebijania = wysokoscPrzebijania;
        this.gazPrzebijania = gazPrzebijania;
        this.cisnieniePrzebijania = cisnieniePrzebijania;
        this.natezeniePrzebieguProcent = natezeniePrzebieguProcent;
        this.mocPrzebijania = mocPrzebijania;
        this.czestotliwoscPrzebijania = czestotliwoscPrzebijania;
        this.szerokoscWiazki = szerokoscWiazki;
        this.focusPrzebijania = focusPrzebijania;
        this.czasPrzebijania = czasPrzebijania;
        this.przedmuch = przedmuch;
    }

    public String getCzasKroku() {
        return czasKroku;
    }

    public void setCzasKroku(String czasKroku) {
        this.czasKroku = czasKroku;
    }

    public String getWysokoscPrzebijania() {
        return wysokoscPrzebijania;
    }

    public void setWysokoscPrzebijania(String wysokoscPrzebijania) {
        this.wysokoscPrzebijania = wysokoscPrzebijania;
    }

    public String getGazPrzebijania() {
        return gazPrzebijania;
    }

    public void setGazPrzebijania(String gazPrzebijania) {
        this.gazPrzebijania = gazPrzebijania;
    }

    public String getCisnieniePrzebijania() {
        return cisnieniePrzebijania;
    }

    public void setCisnieniePrzebijania(String cisnieniePrzebijania) {
        this.cisnieniePrzebijania = cisnieniePrzebijania;
    }

    public String getNatezeniePrzebijaniaProcent() {
        return natezeniePrzebieguProcent;
    }

    public void setNatezeniePrzebieguProcent(String natezeniePrzebieguProcent) {
        this.natezeniePrzebieguProcent = natezeniePrzebieguProcent;
    }

    public String getMocPrzebijania() {
        return mocPrzebijania;
    }

    public void setMocPrzebijania(String mocPrzebijania) {
        this.mocPrzebijania = mocPrzebijania;
    }

    public String getCzestotliwoscPrzebijania() {
        return czestotliwoscPrzebijania;
    }

    public void setCzestotliwoscPrzebijania(String czestotliwoscPrzebijania) {
        this.czestotliwoscPrzebijania = czestotliwoscPrzebijania;
    }

    public String getSzerokoscWiazki() {
        return szerokoscWiazki;
    }

    public void setSzerokoscWiazki(String szerokoscWiazki) {
        this.szerokoscWiazki = szerokoscWiazki;
    }

    public String getFocusPrzebijania() {
        return focusPrzebijania;
    }

    public void setFocusPrzebijania(String focusPrzebijania) {
        this.focusPrzebijania = focusPrzebijania;
    }

    public String getCzasPrzebijania() {
        return czasPrzebijania;
    }

    public void setCzasPrzebijania(String czasPrzebijania) {
        this.czasPrzebijania = czasPrzebijania;
    }

    public String getPrzedmuch() {
        return przedmuch;
    }

    public void setPrzedmuch(String przedmuch) {
        this.przedmuch = przedmuch;
    }
}
