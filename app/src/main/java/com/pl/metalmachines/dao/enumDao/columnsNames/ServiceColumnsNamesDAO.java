package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum ServiceColumnsNamesDAO {
    SERVICE("SER_ID",
            "SER_NUMBER",
            "SER_DATE",
            "SER_ID_CUSTOMER",
            "SER_ID_DEVICE",
            "SER_TYPE",
            "SER_INSPECTION_REPORT",
            "SER_PAYMENT_TYPE",
            "SER_FAULT_DESCRIPTION",
            "SER_RAGNE_OF_WORKS",
            "SER_MATERIAL_USED",
            "SER_COMMENTS",
            "SER_START_DATE",
            "SER_START_TIME",
            "SER_END_DATE",
            "SER_END_TIME",
            "SER_WORKING_TIME",
            "SER_DRIVE_DISTANCE",
            "SER_DAYS_AT_HOTEL",
            "SER_USER",
            "SER_STATE",
            "SER_GPS_LOCATION");

    public String id;
    public String number;
    public String date;
    public String idDevice;
    public String idCustomer;
    public String user;
    public String serviceType;
    public String inspectionReport;
    public String paymentType;
    public String faultDescription;
    public String rangeOfWorks;
    public String materialsUsed;
    public String comments;
    public String startDate;
    public String startTime;
    public String endDate;
    public String endTime;
    public String workingTime;
    public String driveDistance;
    public String daysAtHotel;
    public String state;
    public String gpsLocation;

    ServiceColumnsNamesDAO(String id,
                           String number,
                           String date,
                           String idCustomer,
                           String idDevice,
                           String serviceType,
                           String inspectionReport,
                           String paymentType,
                           String faultDescription,
                           String rangeOfWorks,
                           String materialsUsed,
                           String comments,
                           String startDate,
                           String startTime,
                           String endDate,
                           String endTime,
                           String workingTime,
                           String driveDistance,
                           String daysAtHotel,
                           String user,
                           String state,
                           String gpsLocation) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.idDevice = idDevice;
        this.idCustomer = idCustomer;
        this.user = user;
        this.serviceType = serviceType;
        this.inspectionReport = inspectionReport;
        this.paymentType = paymentType;
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
        this.state = state;
        this.gpsLocation = gpsLocation;
    }
}
