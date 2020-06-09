package com.example.capturevideoandpictureandsaveandchoose.ui.choosedevice;

import java.io.Serializable;

public class ChooseDeviceItemData implements Serializable {


    private String OPCO=""; //作業公司
    private String OPPLD=""; //作業廠處
    private String WAYID =""; //路線代碼
    private String WAYNM =""; //路線名稱
    private String MNTCO=""; //保養公司代碼
    private String MNTFCT=""; //保養廠代碼
    private String MNTFCTNM=""; //保養廠名稱
    private String CONM=""; //公司名稱
    private String CO=""; //公司代碼
    private String PMFCT=""; //生產廠代碼
    private String PMFCTNM=""; //生產廠名稱
    private String EQKD=""; //設備類別代碼
    private String EQKDNM=""; //設備類別名稱
    private String EQNO=""; //設備編號
    private String EQNM=""; //設備名稱
    private String recordDate="";  //紀錄日期
    private String recordSubject=""; //主旨說明 //keynote
    private String filePath=""; //檔案
    private String uploadEMP = ""; //上傳人員代號 //uploadEmployees
    private String UploadNM = ""; //上傳人員姓名
    private String UploadDATETM = ""; //上傳日期時間
    private boolean backgroundChange=false;
    private boolean checkEndItem=false;
    private int progress;

    public String getOPCO() {
        return OPCO;
    }

    public void setOPCO(String OPCO) {
        this.OPCO = OPCO;
    }

    public String getOPPLD() {
        return OPPLD;
    }

    public void setOPPLD(String OPPLD) {
        this.OPPLD = OPPLD;
    }

    public String getWAYID() {
        return WAYID;
    }

    public void setWAYID(String WAYID) {
        this.WAYID = WAYID;
    }

    public String getWAYNM() {
        return WAYNM;
    }

    public void setWAYNM(String WAYNM) {
        this.WAYNM = WAYNM;
    }

    public String getMNTCO() {
        return MNTCO;
    }

    public void setMNTCO(String MNTCO) {
        this.MNTCO = MNTCO;
    }

    public String getMNTFCT() {
        return MNTFCT;
    }

    public void setMNTFCT(String MNTFCT) {
        this.MNTFCT = MNTFCT;
    }

    public String getMNTFCTNM() {
        return MNTFCTNM;
    }

    public void setMNTFCTNM(String MNTFCTNM) {
        this.MNTFCTNM = MNTFCTNM;
    }

    public String getCONM() {
        return CONM;
    }

    public void setCONM(String CONM) {
        this.CONM = CONM;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getPMFCT() {
        return PMFCT;
    }

    public void setPMFCT(String PMFCT) {
        this.PMFCT = PMFCT;
    }

    public String getPMFCTNM() {
        return PMFCTNM;
    }

    public void setPMFCTNM(String PMFCTNM) {
        this.PMFCTNM = PMFCTNM;
    }

    public String getEQKD() {
        return EQKD;
    }

    public void setEQKD(String EQKD) {
        this.EQKD = EQKD;
    }

    public String getEQKDNM() {
        return EQKDNM;
    }

    public void setEQKDNM(String EQKDNM) {
        this.EQKDNM = EQKDNM;
    }

    public String getEQNO() {
        return EQNO;
    }

    public void setEQNO(String EQNO) {
        this.EQNO = EQNO;
    }

    public String getEQNM() {
        return EQNM;
    }

    public void setEQNM(String EQNM) {
        this.EQNM = EQNM;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordSubject() {
        return recordSubject;
    }

    public void setRecordSubject(String recordSubject) {
        this.recordSubject = recordSubject;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadEMP() {
        return uploadEMP;
    }

    public void setUploadEMP(String uploadEMP) {
        this.uploadEMP = uploadEMP;
    }

    public String getUploadNM() {
        return UploadNM;
    }

    public void setUploadNM(String uploadNM) {
        UploadNM = uploadNM;
    }

    public String getUploadDATETM() {
        return UploadDATETM;
    }

    public void setUploadDATETM(String uploadDATETM) {
        UploadDATETM = uploadDATETM;
    }

    public boolean isBackgroundChange() {
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
