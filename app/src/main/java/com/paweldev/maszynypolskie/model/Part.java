package com.paweldev.maszynypolskie.model;


public class Part implements Comparable<Part> {

    int id;
    String name;
    String serialNumber;
    String producer;
    String decsription;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDecsription() {
        return decsription;
    }

    public void setDecsription(String decsription) {
        this.decsription = decsription;
    }


    @Override
    public int compareTo(Part o) {
        return this.name.compareToIgnoreCase(o.name);
    }
}
