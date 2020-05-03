package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum CustomersColumnsNameDAO {
    CUSTOMERS("CUS_ID",
            "CUS_SHORT_NAME",
            "CUS_FULL_NAME",
            "CUS_STREET_ADDRESS",
            "CUS_ZIP_CODE",
            "CUS_CITY",
            "CUS_NIP",
            "CUS_REGON",
            "CUS_PHONE_NUMBER",
            "CUS_EMAIL");

    public String id;
    public String short_name;
    public String full_name;
    public String street_address;
    public String zip_code;
    public String city;
    public String nip;
    public String regon;
    public String phone_number;
    public String email;

    CustomersColumnsNameDAO(String id,
                            String short_name,
                            String full_name,
                            String street_address,
                            String zip_code,
                            String city,
                            String nip,
                            String regon,
                            String phone_number,
                            String email) {
        this.id = id;
        this.short_name = short_name;
        this.full_name = full_name;
        this.street_address = street_address;
        this.zip_code = zip_code;
        this.city = city;
        this.nip = nip;
        this.regon = regon;
        this.phone_number = phone_number;
        this.email = email;
    }
}
