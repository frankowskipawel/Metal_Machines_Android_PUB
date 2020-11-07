package com.paweldev.maszynypolskie.model.enums;

public enum PaymentType {
    PLATNA("PŁATNA"),
    GWARANCYJNA("GWARANCYJNA"),
    DO_DECYZJI_GWARANTA ("DO DECYZJI GWARANTA");

    String name;

    PaymentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
