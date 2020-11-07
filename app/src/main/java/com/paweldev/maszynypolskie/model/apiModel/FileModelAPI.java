package com.paweldev.maszynypolskie.model.apiModel;

public class FileModelAPI {

    private String id;
    private DeviceAPI device;
    private ServiceAPI service;
    private String date;
    private String type;
    private String description;
    private String filename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeviceAPI getDevice() {
        return device;
    }

    public void setDevice(DeviceAPI device) {
        this.device = device;
    }

    public ServiceAPI getService() {
        return service;
    }

    public void setService(ServiceAPI service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
