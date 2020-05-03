package com.pl.metalmachines.dao.enumDao.columnParameters;

public enum ColumnCategoryDAO {
    INT("INT(20)"),
    VARCHAR3000("VARCHAR(3000)"),
    VARCHAR5000("VARCHAR(5000)"),
    VARCHAR255("VARCHAR(255)"),
    VARCHAR20("VARCHAR(20)"),
    DOUBLE("DOUBLE(20)"),
    DATE("DATE");

    public String sqlString;

    ColumnCategoryDAO(String sqlString) {
        this.sqlString = sqlString;
    }
}
