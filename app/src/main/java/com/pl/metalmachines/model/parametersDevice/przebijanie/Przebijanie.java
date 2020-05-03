package com.pl.metalmachines.model.parametersDevice.przebijanie;

public class Przebijanie {
    private Etap etap1;
    private Etap etap2;
    private Etap etap3;

    public Przebijanie(Etap etap1, Etap etap2, Etap etap3) {
        this.etap1 = etap1;
        this.etap2 = etap2;
        this.etap3 = etap3;
    }

    public Etap getEtap1() {
        return etap1;
    }

    public void setEtap1(Etap etap1) {
        this.etap1 = etap1;
    }

    public Etap getEtap2() {
        return etap2;
    }

    public void setEtap2(Etap etap2) {
        this.etap2 = etap2;
    }

    public Etap getEtap3() {
        return etap3;
    }

    public void setEtap3(Etap etap3) {
        this.etap3 = etap3;
    }
}
