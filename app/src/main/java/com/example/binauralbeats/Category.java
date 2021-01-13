package com.example.binauralbeats;

public class Category {
    String catId;
    String catType;
    String catBaseFreq;
    String catBeatFreq;
    String catDesc;
    String catTime;

    public Category(){

    }

    public Category(String catId, String catType, String catBaseFreq, String catBeatFreq, String catDesc, String catTime) {
        this.catId = catId;
        this.catType = catType;
        this.catBaseFreq = catBaseFreq;
        this.catBeatFreq = catBeatFreq;
        this.catDesc = catDesc;
        this.catTime = catTime;
    }

    public String getCatId() {
        return catId;
    }

    public String getCatType() {
        return catType;
    }

    public String getCatBaseFreq() {
        return catBaseFreq;
    }

    public String getCatBeatFreq() {
        return catBeatFreq;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public String getCatTime(){
        return catTime;
    }
}
