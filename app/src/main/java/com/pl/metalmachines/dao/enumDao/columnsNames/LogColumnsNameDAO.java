package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum LogColumnsNameDAO {
    LOG("LOG_ID",
            "LOG_DATE",
            "LOG_USER",
            "LOG_VALUE");

    public String id;
    public String date;
    public String user;
    public String log;

    LogColumnsNameDAO(String id,
                      String date,
                      String user,
                      String log) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.log = log;
    }
}
