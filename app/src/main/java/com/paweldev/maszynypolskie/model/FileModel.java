package com.paweldev.maszynypolskie.model;

import com.paweldev.maszynypolskie.model.enums.FileType;

public class FileModel {
    private String id;
    private String idDevice;
    private String idService;
    private String date;
    private FileType fileType;
    private String description;
    private String filename;

    public FileModel() {
    }

    public FileModel(String id, String idDevice, String idService, String date, FileType fileType, String description, String filename) {
        this.id = id;
        this.idDevice = idDevice;
        this.idService = idService;
        this.date = date;
        this.fileType = fileType;
        this.description = description;
        this.filename = filename;
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

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
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
