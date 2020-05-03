package com.pl.metalmachines.model.parametersDevice;


public class ParametersDevice {

    private String id;
    private String idDevice;
    private String material;
    private String grubosc;
    private String modelDyszy;
    private String gaz;

    StringParameters stringParameters;

    public ParametersDevice(String id, String idDevice, String material, String grubosc, String modelDyszy, String gaz, StringParameters stringParameters) {
        this.id = id;
        this.idDevice = idDevice;
        this.material = material;
        this.grubosc = grubosc;
        this.modelDyszy = modelDyszy;
        this.gaz = gaz;
        this.stringParameters = stringParameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getGrubosc() {
        return grubosc;
    }

    public void setGrubosc(String grubosc) {
        this.grubosc = grubosc;
    }

    public String getModelDyszy() {
        return modelDyszy;
    }

    public void setModelDyszy(String modelDyszy) {
        this.modelDyszy = modelDyszy;
    }

    public String getGaz() {
        return gaz;
    }

    public void setGaz(String gaz) {
        this.gaz = gaz;
    }

    public StringParameters getStringParameters() {
        return stringParameters;
    }

    public void setStringParameters(StringParameters stringParameters) {
        this.stringParameters = stringParameters;
    }
}
