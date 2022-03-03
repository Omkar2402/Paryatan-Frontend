package com.example.sihfrontend.user.monument;

public class monumentInfo {



    private String monumentName;

    private byte[] monumentImage;



    public  monumentInfo(byte[] monumentImage,String monumentName){
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



}
