package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum NoteColumnsNamesDAO {

    NOTE("NOT_ID",
            "NOT_ID_DEVICE",
            "NOT_TEXT");

   public String id;
   public String idDevice;
   public String text;

    NoteColumnsNamesDAO(String id,
                        String idDevice,
                        String text) {
        this.id = id;
        this.idDevice = idDevice;
        this.text = text;
    }
}
