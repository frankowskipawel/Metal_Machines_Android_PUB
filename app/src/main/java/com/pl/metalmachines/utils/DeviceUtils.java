package com.pl.metalmachines.utils;

import com.pl.metalmachines.model.Device;

public class DeviceUtils {

    public static String getDeviceContentAtString(Device device){
        return "S\\N : "+device.serialNumber+
                "\nMoc źródła : "+device.sourcePower +
                "\nKontrahent : " + device.getCustomerName() +
                "\nKategoria : " + device.categoryName +
                "\nData przekazania : " + device.getTransferDate();

    }

    public static Device getBalnkDevice(){
        return new Device("","","","","wybierz...","wybierz...", DateTimeUtils.now());
    }
}
