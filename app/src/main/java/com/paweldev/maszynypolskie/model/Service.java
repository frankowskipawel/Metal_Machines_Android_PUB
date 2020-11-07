package com.paweldev.maszynypolskie.model;


import com.paweldev.maszynypolskie.model.enums.PaymentType;
import com.paweldev.maszynypolskie.model.enums.ServiceType;

import java.util.List;

public class Service {
    private String id;
    private String number;
    private String year;
    private String date;
    private Customer customer;
    private Device device;
    private List<ServiceType> type;
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
    private String gpsLocation;

    public Service(String id, String number, String year, String date, Customer customer, Device device, List<ServiceType> type, PaymentType paymentType, DeviceInspectionReport deviceInspectionReport, String faultDescription, String rangeOfWorks, String materialsUsed, String comments, String startDate, String startTime, String endDate, String endTime, String workingTime, String driveDistance, String daysAtHotel, String user, String state, String gpsLocation) {
        this.id = id;
        this.number = number;
        this.year = year;
        this.date = date;
        this.customer = customer;
        this.device = device;
        this.type = type;
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
        this.gpsLocation = gpsLocation;
    }

    public Service(String id, String number, String date, Customer customer, Device device, List<ServiceType> type, PaymentType paymentType, DeviceInspectionReport deviceInspectionReport, String faultDescription, String rangeOfWorks, String materialsUsed, String comments, String startDate, String startTime, String endDate, String endTime, String workingTime, String driveDistance, String daysAtHotel, String user, String state) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.customer = customer;
        this.device = device;
        this.type = type;
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

    public Service(String id, String number, String date, Customer customer, Device device, List<ServiceType> type, PaymentType paymentType, DeviceInspectionReport deviceInspectionReport, String faultDescription, String rangeOfWorks, String materialsUsed, String comments, String startDate, String startTime, String endDate, String endTime, String workingTime, String driveDistance, String daysAtHotel, String user, String state, String gpsLocation) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.customer = customer;
        this.device = device;
        this.type = type;
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
        this.gpsLocation = gpsLocation;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<ServiceType> getType() {
        return type;
    }

    public void setType(List<ServiceType> type) {
        this.type = type;
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
                ", year='" + year + '\'' +
                ", date='" + date + '\'' +
                ", customer=" + customer +
                ", device=" + device +
                ", type=" + type +
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
                ", gpsLocation='" + gpsLocation + '\'' +
                '}';
    }
}
