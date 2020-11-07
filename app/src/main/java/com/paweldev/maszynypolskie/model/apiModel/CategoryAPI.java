package com.paweldev.maszynypolskie.model.apiModel;

public class CategoryAPI {

    private String id;
    private String name;
    private String serviceReviewOperationsList;
    private String operatorTrainingList;

    public CategoryAPI(String id, String name, String serviceReviewOperationsList, String operatorTrainingList) {
        this.id = id;
        this.name = name;
        this.serviceReviewOperationsList = serviceReviewOperationsList;
        this.operatorTrainingList = operatorTrainingList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceReviewOperationsList() {
        return serviceReviewOperationsList;
    }

    public void setServiceReviewOperationsList(String serviceReviewOperationsList) {
        this.serviceReviewOperationsList = serviceReviewOperationsList;
    }

    public String getOperatorTrainingList() {
        return operatorTrainingList;
    }

    public void setOperatorTrainingList(String operatorTrainingList) {
        this.operatorTrainingList = operatorTrainingList;
    }

    @Override
    public String toString() {
        return "CategoryAPI{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", serviceReviewOperationsList='" + serviceReviewOperationsList + '\'' +
                ", operatorTrainingList='" + operatorTrainingList + '\'' +
                '}';
    }
}