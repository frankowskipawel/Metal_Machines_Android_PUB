package com.paweldev.maszynypolskie.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Objects;

public class Device implements Comparable {
    private String id;
    private String name;
    private String serialNumber;
    private String sourcePower;
    private String customerId;
    private String categoryId;
    private String transferDate;
    private String customerName;
    private String categoryName;
    private String gpsLocation;
    private String streetAddress;
    private String zipCode;
    private String city;
    private List<Part> parts;


    public Device(String id, String name, String serialNumber, String sourcePower, String customerId, String categoryId, String transferDate, String streetAddress, String zipCode, String city,  List<Part> parts) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.sourcePower = sourcePower;
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.transferDate = transferDate;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.parts = parts;
    }

    public Device(String id, String name, String serialNumber, String sourcePower, String customerId, String categoryId, String transferDate, String gpsLocation, String streetAddress, String zipCode, String city,  List<Part> parts) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.sourcePower = sourcePower;
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.transferDate = transferDate;
        this.gpsLocation = gpsLocation;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.parts = parts;
    }

    public Device(String id, String name, String serialNumber, String sourcePower, String customerId, String categoryId, String transferDate, String streetAddress, String zipCode, String city) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.sourcePower = sourcePower;
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.transferDate = transferDate;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSourcePower() {
        return sourcePower;
    }

    public void setSourcePower(String sourcePower) {
        this.sourcePower = sourcePower;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public int compareTo(Object o) {
        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id) &&
                Objects.equals(name, device.name) &&
                Objects.equals(serialNumber, device.serialNumber) &&
                Objects.equals(sourcePower, device.sourcePower) &&
                Objects.equals(customerId, device.customerId) &&
                Objects.equals(categoryId, device.categoryId) &&
                Objects.equals(transferDate, device.transferDate) &&
                Objects.equals(customerName, device.customerName) &&
                Objects.equals(categoryName, device.categoryName) &&
                Objects.equals(gpsLocation, device.gpsLocation) &&
                Objects.equals(streetAddress, device.streetAddress) &&
                Objects.equals(zipCode, device.zipCode) &&
                Objects.equals(city, device.city);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, serialNumber, sourcePower, customerId, categoryId, transferDate, customerName, categoryName, gpsLocation, streetAddress, zipCode, city);
    }
}
