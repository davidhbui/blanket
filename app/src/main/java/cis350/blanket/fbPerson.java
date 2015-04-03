package cis350.blanket;

import java.util.ArrayList;

/**
 * Created by aashishlalani on 4/2/15.
 */

public class fbPerson {

    private int _id;
    private String fullName;
    private String pin;
    private double donationAmount;
    private String ccNumber;
    private String donationInfo;

    public fbPerson() {

    }

    public fbPerson(int id, String fullName, String pin, String ccNumber) {
        donationAmount = 0.0;
        this._id = id;
        this.fullName = fullName;
        this.pin = pin;
        this.ccNumber = ccNumber;
        donationInfo = "";
    }


    public void setFullName(String name) {
        fullName = name;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(double amount) {
        donationAmount = amount;
    }

    public void setDonationInfo(String info) {
        donationInfo = info;
    }

    public String getDonationInfo() {
        return donationInfo;
    }


    public String getPersonName() {
        return this.fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getPin() {
        return pin;
    }

    public void setCcNumber(String cc) {
        this.ccNumber = cc;
    }

    public String getCcNumber() {
        return ccNumber;
    }

}
