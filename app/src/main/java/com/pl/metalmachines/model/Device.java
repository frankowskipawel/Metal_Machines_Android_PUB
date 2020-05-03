package com.pl.metalmachines.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Device implements Comparable {
    public String id;
    public String name;
    public String serialNumber;
    public String sourcePower;
    public String customerId;
    public String categoryId;
    public String transferDate;
    public String customerName;
    public String categoryName;

    public Device(String id, String name, String serialNumber, String sourcePower, String customerId, String categoryId, String transferDate) {
        this.id = id;
        this.name = name.toUpperCase();
        this.serialNumber = serialNumber;
        this.sourcePower = sourcePower;
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.transferDate = transferDate;
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

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", sourcePower='" + sourcePower + '\'' +
                ", customerId='" + customerId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", transferDate='" + transferDate + '\'' +
                ", customerName='" + customerName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
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
                Objects.equals(transferDate, device.transferDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, serialNumber, sourcePower, customerId, categoryId, transferDate);
    }
}
