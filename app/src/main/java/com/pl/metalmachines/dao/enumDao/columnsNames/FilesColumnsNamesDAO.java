package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum FilesColumnsNamesDAO {
    FILES("FIL_ID",
            "FIL_ID_DEVICE",
            "FIL_ID_SERVICE",
            "FIL_DATE",
            "FIL_TYPE",
            "FIL_DESCRIPTION",
            "FIL_FILENAME");

    public String id;
    public String idDevice;
    public String idService;
    public String date;
    public String type;
    public String description;
    public String fileName;

    FilesColumnsNamesDAO(String id,
                         String idDevice,
                         String idService,
                         String date,
                         String type,
                         String description,
                         String fileName) {
        this.id = id;
        this.idDevice = idDevice;
        this.idService = idService;
        this.date = date;
        this.type = type;
        this.description = description;
        this.fileName = fileName;
    }
}
