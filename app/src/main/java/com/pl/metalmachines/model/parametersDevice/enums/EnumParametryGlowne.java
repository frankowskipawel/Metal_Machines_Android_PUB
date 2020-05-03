package com.pl.metalmachines.model.parametersDevice.enums;

public enum EnumParametryGlowne {
    PARAMETRY_GLOWNE("Parametry główne"),
    MATERIAL("Materiał"),
    GRUBOSC("Grubość"),
    MODEL_DYSZY("Model dyszy"),
    GAZ("Gaz");

    String name;

    EnumParametryGlowne(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
