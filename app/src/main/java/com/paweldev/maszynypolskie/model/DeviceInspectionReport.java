package com.paweldev.maszynypolskie.model;

import java.util.LinkedHashMap;

public class DeviceInspectionReport {

    String idService;
    String periodicService;
    String description;
    LinkedHashMap<String, String> serviceOperations;

    public DeviceInspectionReport(String idService, String periodicService, String description, LinkedHashMap<String, String> serviceOperations) {
        this.idService = idService;
        this.periodicService = periodicService;
        this.description = description;
        this.serviceOperations = serviceOperations;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getPeriodicService() {
        return periodicService;
    }

    public void setPeriodicService(String periodicService) {
        this.periodicService = periodicService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedHashMap<String, String> getServiceOperations() {
        return serviceOperations;
    }

    public void setServiceOperations(LinkedHashMap<String, String> serviceOperations) {
        this.serviceOperations = serviceOperations;
    }

    @Override
    public String toString() {
        return "DeviceInspectionReport{" +
                "idService='" + idService + '\'' +
                ", periodicService='" + periodicService + '\'' +
                ", description='" + description + '\'' +
                ", serviceOperations=" + serviceOperations +
                '}';
    }
}
