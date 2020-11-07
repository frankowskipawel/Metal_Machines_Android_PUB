package com.paweldev.maszynypolskie.model;

public class DeviceNote {
    String id;
    String idDevice;
    String text;

    public DeviceNote(String id, String idDevice, String text)  {
        this.id = id;
        this.idDevice = idDevice;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DeviceNote{" +
                "id='" + id + '\'' +
                ", idDevice='" + idDevice + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
