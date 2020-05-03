package com.pl.metalmachines.model.parametersDevice;

import com.pl.metalmachines.model.parametersDevice.ciecie.Ciecie;
import com.pl.metalmachines.model.parametersDevice.przebijanie.Przebijanie;
import com.pl.metalmachines.model.parametersDevice.wpalenie.Wpalenie;

public class StringParameters {

    private Ciecie ciecie;
    private Przebijanie przebijanie;
    private Wpalenie wpalenie;

    public StringParameters(Ciecie ciecie, Przebijanie przebijanie, Wpalenie wpalenie) {
        this.ciecie = ciecie;
        this.przebijanie = przebijanie;
        this.wpalenie = wpalenie;
    }

    public Ciecie getCiecie() {
        return ciecie;
    }

    public void setCiecie(Ciecie ciecie) {
        this.ciecie = ciecie;
    }

    public Przebijanie getPrzebijanie() {
        return przebijanie;
    }

    public void setPrzebijanie(Przebijanie przebijanie) {
        this.przebijanie = przebijanie;
    }

    public Wpalenie getWpalenie() {
        return wpalenie;
    }

    public void setWpalenie(Wpalenie wpalenie) {
        this.wpalenie = wpalenie;
    }

    @Override
    public String toString() {
        return "StringParameters{" +
                "ciecie=" + ciecie +
                ", przebijanie=" + przebijanie +
                ", wpalenie=" + wpalenie +
                '}';
    }
}
