package com.pl.metalmachines.dao.enumDao.columnsNames;

public enum CategoryColumnsNamesDAO {
    CATEGORY("CAT_ID",
            "CAT_NAME",
            "CAT_SERVICE_REVIEW_OPERATIONS_LIST",
            "CAT_OPERATOR_TRAINING_LIST");

    public String id;
    public String categoryName;
    public String serviceOperationsList;
    public String operatorTrainingList;

    CategoryColumnsNamesDAO(String id,
                            String categoryName,
                            String serviceOperationsList,
                            String operatorTrainingList) {
        this.id = id;
        this.categoryName = categoryName;
        this.serviceOperationsList = serviceOperationsList;
        this.operatorTrainingList = operatorTrainingList;
    }
}
