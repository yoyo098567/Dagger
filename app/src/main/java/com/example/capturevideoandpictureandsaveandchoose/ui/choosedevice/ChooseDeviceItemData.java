package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import java.io.Serializable;

public class ChooseDeviceItemData implements Serializable {
    private String recordDate="";
    private String maintenancePlant="";
    private String maintenancePlantCompanyId="";
    private String maintenancePlantId="";
    private String company="";
    private String companyId="";
    private String productionPlant="";
    private String productionPlantId="";
    private String deviceCategory="";
    private String deviceCategryId="";
    private String deciceId="";
    private String deciceName="";
    private String keynote="";
    private String filePath="";
    private String uploadEmployees="";
    private boolean backgroundChange=false;
    private boolean checkEndItem=false;
    public String getMaintenancePlantCompanyId() {
        return maintenancePlantCompanyId;
    }

    public void setMaintenancePlantCompanyId(String maintenancePlantCompanyId) {
        this.maintenancePlantCompanyId = maintenancePlantCompanyId;
    }

    public String getMaintenancePlantId() {
        return maintenancePlantId;
    }

    public void setMaintenancePlantId(String maintenancePlantId) {
        this.maintenancePlantId = maintenancePlantId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProductionPlantId() {
        return productionPlantId;
    }

    public void setProductionPlantId(String productionPlantId) {
        this.productionPlantId = productionPlantId;
    }

    public String getDeviceCategryId() {
        return deviceCategryId;
    }

    public void setDeviceCategryId(String deviceCategryId) {
        this.deviceCategryId = deviceCategryId;
    }

    public String getDeciceName() {
        return deciceName;
    }

    public void setDeciceName(String deciceName) {
        this.deciceName = deciceName;
    }

    public boolean isBackgroundChange() {
        return backgroundChange;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getMaintenancePlant() {
        return maintenancePlant;
    }

    public void setMaintenancePlant(String maintenancePlant) {
        this.maintenancePlant = maintenancePlant;
    }

    public String getProductionPlant() {
        return productionPlant;
    }

    public void setProductionPlant(String productionPlant) {
        this.productionPlant = productionPlant;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getDeciceId() {
        return deciceId;
    }

    public void setDeciceId(String deciceId) {
        this.deciceId = deciceId;
    }

    public String getKeynote() {
        return keynote;
    }

    public void setKeynote(String keynote) {
        this.keynote = keynote;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadEmployees() {
        return uploadEmployees;
    }

    public void setUploadEmployees(String uploadEmployees) {
        this.uploadEmployees = uploadEmployees;
    }

    public boolean getBackgroundChange() {
        return backgroundChange;
    }

    public void setBackgroundChange(boolean backgroundChange) {
        this.backgroundChange = backgroundChange;
    }

    public boolean isCheckEndItem() {
        return checkEndItem;
    }

    public void setCheckEndItem(boolean checkEndItem) {
        this.checkEndItem = checkEndItem;
    }
}
