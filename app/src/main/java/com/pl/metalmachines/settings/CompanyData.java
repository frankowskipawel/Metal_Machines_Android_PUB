package com.pl.metalmachines.settings;

public class CompanyData {

    public static final String COMPANY_NAME = "METAL MACHINERY COMPANY";
    public static final String COMPANY_ADDRESS_STREET = "Prosta 23";
    public static final String COMPANY_ADDRESS_ZIP_CODE = "00-000";
    public static final String COMPANY_ADDRESS_CITY = "Warszawa";
    public static final String COMPANY_NIP = "000-00-00-000";
    public static final String COMPANY_REGON = "0000000";
    public static final String COMPANY_KRS = "0000000000";
    public static final String[] COMPANY_PHONES= {"+48 000 000 000", "+48 000 000 000"};
    public static final String[] COMPANY_PHONES_SERVICE = {"+48 000 000 000"};

    public static String getFullCompanyData(){
        return COMPANY_NAME+"\n"+
                COMPANY_ADDRESS_STREET+"\n"+
                COMPANY_ADDRESS_ZIP_CODE+" "+COMPANY_ADDRESS_CITY+"\n"+
                "NIP: "+COMPANY_NIP+"\n"+
                "REGON: "+COMPANY_REGON+"\n"+
                "KRS: "+COMPANY_KRS+"\n"+
                "tel. "+COMPANY_PHONES[0]+"\n"+
                "tel. "+COMPANY_PHONES[1]+"\n"+
                "serwis "+COMPANY_PHONES_SERVICE[0];

    }
}
