package com.pl.metalmachines.model.parametersDevice.wpalenie;

public class Wpalenie {

    ParametryWpalenia parametryWpalenia;
    WpalajWolno wpalajWolno;
    WpalajKolem wpalajKolem;

    public Wpalenie(ParametryWpalenia parametryWpalenia, WpalajWolno wpalajWolno, WpalajKolem wpalajKolem) {
        this.parametryWpalenia = parametryWpalenia;
        this.wpalajWolno = wpalajWolno;
        this.wpalajKolem = wpalajKolem;
    }

    public ParametryWpalenia getParametryWpalenia() {
        return parametryWpalenia;
    }

    public void setParametryWpalenia(ParametryWpalenia parametryWpalenia) {
        this.parametryWpalenia = parametryWpalenia;
    }

    public WpalajWolno getWpalajWolno() {
        return wpalajWolno;
    }

    public void setWpalajWolno(WpalajWolno wpalajWolno) {
        this.wpalajWolno = wpalajWolno;
    }

    public WpalajKolem getWpalajKolem() {
        return wpalajKolem;
    }

    public void setWpalajKolem(WpalajKolem wpalajKolem) {
        this.wpalajKolem = wpalajKolem;
    }

    @Override
    public String toString() {
        return "Wpalenie{" +
                "parametryWpalenia=" + parametryWpalenia +
                ", wpalajWolno=" + wpalajWolno +
                ", wpalajKolem=" + wpalajKolem +
                '}';
    }
}
