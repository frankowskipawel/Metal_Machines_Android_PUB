package com.paweldev.maszynypolskie.utils;

import com.paweldev.maszynypolskie.model.Device;

public class DeviceUtils {

    public static String getDeviceContentAtString(Device device){
        return "S\\N : "+device.getSerialNumber()+
                "\nMoc źródła : "+device.getSourcePower() +
                "\nKontrahent : " + device.getCustomerName() +
                "\nKategoria : " + device.getCategoryName() +
                "\nData przekazania : " + device.getTransferDate()+
                "\nAdres : " + device.getStreetAddress()+", "+device.getZipCode()+" "+device.getCity();

    }

    public static Device getBalnkDevice(){
        return new Device("","","","","wybierz...","wybierz...", DateTimeUtils.now(), "", "", "");
    }
}
