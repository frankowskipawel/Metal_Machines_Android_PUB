package com.paweldev.maszynypolskie.model;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Customer {

    private String id;
    private String shortName;
    private String fullName;
    private String streetAddress;
    private String zipCode;
    private String city;
    private String nip;
    private String regon;
    private String phoneNumber;
    private String email;

    public Customer(String id, String shortName, String fullName, String streetAddress, String zipCode, String city, String nip, String regon, String phoneNumber, String email) {
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.streetAddress = streetAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.nip = nip;
        this.regon = regon;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "shortName='" + shortName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", nip='" + nip + '\'' +
                ", regon='" + regon + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(shortName, customer.shortName) &&
                Objects.equals(fullName, customer.fullName) &&
                Objects.equals(streetAddress, customer.streetAddress) &&
                Objects.equals(zipCode, customer.zipCode) &&
                Objects.equals(city, customer.city) &&
                Objects.equals(nip, customer.nip) &&
                Objects.equals(regon, customer.regon) &&
                Objects.equals(phoneNumber, customer.phoneNumber) &&
                Objects.equals(email, customer.email);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, shortName, fullName, streetAddress, zipCode, city, nip, regon, phoneNumber, email);
    }
}

