package com.example.sihfrontend.user.monument;

import java.sql.Time;

public class monumentInfo {



    private String monumentName;

    private byte[] monumentImage;

    private String monumentDesc;

    private String location;
    private double foreign_child;
    private double foreign_adult;
    private double indian_child;
    private double indian_adult;
    private String closeTime;
    private  String  monumentLink;
    private String sartTime;
    private  byte[] monumentVideo;


    public monumentInfo(String monumentName, byte[] monumentImage, String monumentDesc, String location, double foreign_child, double foreign_adult, double indian_child, double indian_adult, String closeTime, String monumentLink, String sartTime, byte[] monumentVideo) {
        this.monumentName = monumentName;
        this.monumentImage = monumentImage;
        this.monumentDesc = monumentDesc;
        this.location = location;
        this.foreign_child = foreign_child;
        this.foreign_adult = foreign_adult;
        this.indian_child = indian_child;
        this.indian_adult = indian_adult;
        this.closeTime = closeTime;
        this.monumentLink = monumentLink;
        this.sartTime = sartTime;
        this.monumentVideo = monumentVideo;
    }

    public  monumentInfo(byte[] monumentImage, String monumentName){
        this.monumentName = monumentName;
        this.monumentImage = monumentImage;
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


    public String getMonumentDesc() {
        return monumentDesc;
    }

    public void setMonumentDesc(String monumentDesc) {
        this.monumentDesc = monumentDesc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getForeign_child() {
        return foreign_child;
    }

    public void setForeign_child(double foreign_child) {
        this.foreign_child = foreign_child;
    }

    public double getForeign_adult() {
        return foreign_adult;
    }

    public void setForeign_adult(double foreign_adult) {
        this.foreign_adult = foreign_adult;
    }

    public double getIndian_child() {
        return indian_child;
    }

    public void setIndian_child(double indian_child) {
        this.indian_child = indian_child;
    }

    public double getIndian_adult() {
        return indian_adult;
    }

    public void setIndian_adult(double indian_adult) {
        this.indian_adult = indian_adult;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getMonumentLink() {
        return monumentLink;
    }

    public void setMonumentLink(String monumentLink) {
        this.monumentLink = monumentLink;
    }

    public String getSartTime() {
        return sartTime;
    }

    public void setSartTime(String sartTime) {
        this.sartTime = sartTime;
    }

    public byte[] getMonumentVideo() {
        return monumentVideo;
    }

    public void setMonumentVideo(byte[] monumentVideo) {
        this.monumentVideo = monumentVideo;
    }
}
