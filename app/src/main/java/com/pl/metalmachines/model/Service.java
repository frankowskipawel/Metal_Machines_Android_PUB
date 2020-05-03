package com.pl.metalmachines.model;


import com.pl.metalmachines.model.enums.PaymentType;
import com.pl.metalmachines.model.enums.ServiceType;

import java.util.List;

public class Service {
    private String id;
    private String number;
    private String date;
    private String idCustomer;
    private String idDevice;
    private List<ServiceType> serviceType;
    private PaymentType paymentType;
    private DeviceInspectionReport deviceInspectionReport;
    private String faultDescription;
    private String rangeOfWorks;
    private String materialsUsed;
    private String comments;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String workingTime;
    private String driveDistance;
    private String daysAtHotel;
    private String user;
    private String state;

    public Service(String id, String number, String date, String idCustomer, String idDevice, List<ServiceType> serviceType, PaymentType paymentType, DeviceInspectionReport deviceInspectionReport, String faultDescription, String rangeOfWorks, String materialsUsed, String comments, String startDate, String startTime, String endDate, String endTime, String workingTime, String driveDistance, String daysAtHotel, String user, String state) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.idCustomer = idCustomer;
        this.idDevice = idDevice;
        this.serviceType = serviceType;
        this.paymentType = paymentType;
        this.deviceInspectionReport = deviceInspectionReport;
        this.faultDescription = faultDescription;
        this.rangeOfWorks = rangeOfWorks;
        this.materialsUsed = materialsUsed;
        this.comments = comments;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.workingTime = workingTime;
        this.driveDistance = driveDistance;
        this.daysAtHotel = daysAtHotel;
        this.user = user;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public List<ServiceType> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<ServiceType> serviceType) {
        this.serviceType = serviceType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public DeviceInspectionReport getDeviceInspectionReport() {
        return deviceInspectionReport;
    }

    public void setDeviceInspectionReport(DeviceInspectionReport deviceInspectionReport) {
        this.deviceInspectionReport = deviceInspectionReport;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getRangeOfWorks() {
        return rangeOfWorks;
    }

    public void setRangeOfWorks(String rangeOfWorks) {
        this.rangeOfWorks = rangeOfWorks;
    }

    public String getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(String materialsUsed) {
        this.materialsUsed = materialsUsed;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getDriveDistance() {
        return driveDistance;
    }

    public void setDriveDistance(String driveDistance) {
        this.driveDistance = driveDistance;
    }

    public String getDaysAtHotel() {
        return daysAtHotel;
    }

    public void setDaysAtHotel(String daysAtHotel) {
        this.daysAtHotel = daysAtHotel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", date='" + date + '\'' +
                ", idCustomer='" + idCustomer + '\'' +
                ", idDevice='" + idDevice + '\'' +
                ", serviceType=" + serviceType +
                ", paymentType=" + paymentType +
                ", deviceInspectionReport=" + deviceInspectionReport +
                ", faultDescription='" + faultDescription + '\'' +
                ", rangeOfWorks='" + rangeOfWorks + '\'' +
                ", materialsUsed='" + materialsUsed + '\'' +
                ", comments='" + comments + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endTime='" + endTime + '\'' +
                ", workingTime='" + workingTime + '\'' +
                ", driveDistance='" + driveDistance + '\'' +
                ", daysAtHotel='" + daysAtHotel + '\'' +
                ", user='" + user + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
