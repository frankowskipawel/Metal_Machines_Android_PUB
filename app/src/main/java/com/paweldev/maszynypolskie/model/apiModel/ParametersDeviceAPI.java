package com.paweldev.maszynypolskie.model.apiModel;

public class ParametersDeviceAPI {

    private String id;
    private String idDevice;
    private String material;
    private String grubosc;
    private String modelDyszy;
    private String gaz;
    private String parametersInJSON;

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

    public String getStringParameters() {
        return parametersInJSON;
    }

    public void setStringParameters(String parametersInJSON) {
        this.parametersInJSON = parametersInJSON;
    }
}
