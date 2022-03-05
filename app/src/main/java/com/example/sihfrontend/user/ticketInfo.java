package com.example.sihfrontend.user;

import java.util.Date;

public class ticketInfo {
    String monumentName;
    Date dateOfVisit;
    String verificationId;
    String gender;
    String age;
    String nationality;
    String visitorName;

    public ticketInfo() {
    }

    public ticketInfo(String monumentName, Date dateOfVisit, String verificationId, String gender, String age, String nationality, String visitorName) {
        this.monumentName = monumentName;
        this.dateOfVisit = dateOfVisit;
        this.verificationId = verificationId;
        this.gender = gender;
        this.age = age;
        this.nationality = nationality;
        this.visitorName = visitorName;
    }

    public String getMonumentName() {
        return monumentName;
    }

    public void setMonumentName(String monumentName) {
        this.monumentName = monumentName;
    }

    public Date getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Date dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    @Override
    public String toString() {
        return "ticketInfo{" +
                "monumentName='" + monumentName + '\'' +
                ", dateOfVisit=" + dateOfVisit +
                ", verificationId='" + verificationId + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
