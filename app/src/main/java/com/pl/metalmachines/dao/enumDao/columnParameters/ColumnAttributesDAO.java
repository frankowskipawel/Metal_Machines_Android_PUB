package com.pl.metalmachines.dao.enumDao.columnParameters;

public enum ColumnAttributesDAO {
    NULL("NULL"),
    NOT_NULL("NOT NULL"),
    UNIQUE("UNIQUE");

    public String sqlString;

    ColumnAttributesDAO(String sqlString) {
        this.sqlString = sqlString;
    }
}
