package com.example.sihfrontend.user.monument;

public class monumentInfo {

    private int monumentId;

    private int adminId;

    private byte[] monumentPreview;

    private String openingTime;

    private String closingTime;

    private String monumentName;

    private byte[] monumentImage;

    private byte[] monumentPOA;

    private String monumentDescription;

    private double indianAdultFare;

    private double indianChildFare;


    private double foreignAdultFare;

    private double foreignChildFare;

    private String closedDay;

    private String websiteLink;

    private String monumentType;

    private String monumentLocation;

    public monumentInfo(int monumentId, int adminId, byte[] monumentPreview, String openingTime, String closingTime, String monumentName, byte[] monumentImage, byte[] monumentPOA, String monumentDescription, double indianAdultFare, double indianChildFare, double foreignAdultFare, double foreignChildFare, String closedDay, String websiteLink, String monumentType, String monumentLocation) {
        this.monumentId = monumentId;
        this.adminId = adminId;
        this.monumentPreview = monumentPreview;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.monumentName = monumentName;
        this.monumentImage = monumentImage;
        this.monumentPOA = monumentPOA;
        this.monumentDescription = monumentDescription;
        this.indianAdultFare = indianAdultFare;
        this.indianChildFare = indianChildFare;
        this.foreignAdultFare = foreignAdultFare;
        this.foreignChildFare = foreignChildFare;
        this.closedDay = closedDay;
        this.websiteLink = websiteLink;
        this.monumentType = monumentType;
        this.monumentLocation = monumentLocation;
    }

    public int getMonumentId() {
        return monumentId;
    }

    public void setMonumentId(int monumentId) {
        this.monumentId = monumentId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public byte[] getMonumentPreview() {
        return monumentPreview;
    }

    public void setMonumentPreview(byte[] monumentPreview) {
        this.monumentPreview = monumentPreview;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getMonumentName() {
        return monumentName;
    }

    public void setMonumentName(String monumentName) {
        this.monumentName = monumentName;
    }

    public byte[] getMonumentImage() {
        return monumentImage;
    }

    public void setMonumentImage(byte[] monumentImage) {
        this.monumentImage = monumentImage;
    }

    public byte[] getMonumentPOA() {
        return monumentPOA;
    }

    public void setMonumentPOA(byte[] monumentPOA) {
        this.monumentPOA = monumentPOA;
    }

    public String getMonumentDescription() {
        return monumentDescription;
    }

    public void setMonumentDescription(String monumentDescription) {
        this.monumentDescription = monumentDescription;
    }

    public double getIndianAdultFare() {
        return indianAdultFare;
    }

    public void setIndianAdultFare(double indianAdultFare) {
        this.indianAdultFare = indianAdultFare;
    }

    public double getIndianChildFare() {
        return indianChildFare;
    }

    public void setIndianChildFare(double indianChildFare) {
        this.indianChildFare = indianChildFare;
    }

    public double getForeignAdultFare() {
        return foreignAdultFare;
    }

    public void setForeignAdultFare(double foreignAdultFare) {
        this.foreignAdultFare = foreignAdultFare;
    }

    public double getForeignChildFare() {
        return foreignChildFare;
    }

    public void setForeignChildFare(double foreignChildFare) {
        this.foreignChildFare = foreignChildFare;
    }

    public String getClosedDay() {
        return closedDay;
    }

    public void setClosedDay(String closedDay) {
        this.closedDay = closedDay;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getMonumentType() {
        return monumentType;
    }

    public void setMonumentType(String monumentType) {
        this.monumentType = monumentType;
    }

    public String getMonumentLocation() {
        return monumentLocation;
    }

    public void setMonumentLocation(String monumentLocation) {
        this.monumentLocation = monumentLocation;
    }

}
