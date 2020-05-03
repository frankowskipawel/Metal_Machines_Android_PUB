package com.pl.metalmachines.model.parametersDevice.ciecie;



public class Ciecie {

   private ParametryGlowne parametryGlowne;
   private RuszWolno ruszWolno;
   private StopWolno stopWolno;

    public Ciecie(ParametryGlowne parametryGlowne, RuszWolno ruszWolno, StopWolno stopWolno) {
        this.parametryGlowne = parametryGlowne;
        this.ruszWolno = ruszWolno;
        this.stopWolno = stopWolno;
    }

    public ParametryGlowne getParametryGlowne() {
        return parametryGlowne;
    }

    public void setParametryGlowne(ParametryGlowne parametryGlowne) {
        this.parametryGlowne = parametryGlowne;
    }

    public RuszWolno getRuszWolno() {
        return ruszWolno;
    }

    public void setRuszWolno(RuszWolno ruszWolno) {
        this.ruszWolno = ruszWolno;
    }

    public StopWolno getStopWolno() {
        return stopWolno;
    }

    public void setStopWolno(StopWolno stopWolno) {
        this.stopWolno = stopWolno;
    }
}
