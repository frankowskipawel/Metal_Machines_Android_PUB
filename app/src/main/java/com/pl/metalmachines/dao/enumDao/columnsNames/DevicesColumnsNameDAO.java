package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum DevicesColumnsNameDAO {
    DEVICES("DEV_ID",
            "DEV_NAME",
            "DEV_SERIAL_NUMBER",
            "DEV_SOURCE_POWER",
            "DEV_CUSTOMER_ID",
            "DEV_CATEGORY_ID",
            "DEV_TRANSFER_DATE");

    public String id;
    public String name;
    public String serialNumber;
    public String sourcePower;
    public String customer_id;
    public String category_id;
    public String transferDate;

    DevicesColumnsNameDAO(String id,
                          String name,
                          String serialNumber,
                          String sourcePower,
                          String customer_id,
                          String category_id,
                          String transferDate) {
        this.id = id;
        this.name = name;
        this.serialNumber = serialNumber;
        this.sourcePower = sourcePower;
        this.customer_id = customer_id;
        this.category_id = category_id;
        this.transferDate = transferDate;
    }
}
