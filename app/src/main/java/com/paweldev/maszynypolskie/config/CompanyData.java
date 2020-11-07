package com.paweldev.maszynypolskie.config;

public class CompanyData {

    public static final String COMPANY_NAME = "MASZYNY-POLSKIE.PL Sp. z o.o.";
    public static final String COMPANY_ADDRESS_STREET = "M. Skłodowskiej-Curie 41";
    public static final String COMPANY_ADDRESS_ZIP_CODE = "87-100";
    public static final String COMPANY_ADDRESS_CITY = "Toruń";
    public static final String COMPANY_NIP = "879-269-13-65";
    public static final String COMPANY_REGON = "367062732";
    public static final String COMPANY_KRS = "0000674129";
    public static final String[] COMPANY_PHONES= {"+48 604 820 313", "+48 512 426 126"};
    public static final String[] COMPANY_PHONES_SERVICE = {"+48 501 448 351"};

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
