package com.fdmgroup.springbootdemo.dto;

public class GeocoderResponse {

    private Standard standard;
    private String latt;
    private String longt;
    private String postal;
    private String remaining_credits;

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getRemaining_credits() {
        return remaining_credits;
    }

    public void setRemaining_credits(String remaining_credits) {
        this.remaining_credits = remaining_credits;
    }
}