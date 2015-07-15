package cis350.blanket;

import android.location.Location;

import java.sql.Date;
import java.text.NumberFormat;

/**
 * Local representation of Donation objects in the Parse database
 *
 * Created by davidhb on 4/14/2015.
 */
public class Donation {

    // Item donated
    private String item;

    // Date and time of donation
    private long date;
    // Convert it to a date: Date date = new Date(date);

    // Amount donated
    private double amount;

    // Recipient of donation
    private String recipientID;
    private String recipientName;

    // User who made the donation
    private String userID;
    private String donorName;

    // has it been redeemed yet?
    private boolean redeemed;

    // location
    private Location location;


    public Donation(String recipientID, String item, long date, double amount, Location location) {
        this.item = item;
        this.date = date;
        this.amount = amount;
        this.recipientID = recipientID;
        this.location = location;
        redeemed = false;
    }

    public void setRecipientName(String r) {
        recipientName = r;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setDonorName(String s) {
        donorName = s;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getItem() {
        return item;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRedeemed(boolean b) {
        redeemed = b;
    }

    public boolean getRedeemed() {
        return redeemed;
    }

    public String getRedeemedString() {
        if (redeemed) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public double getAmount() {
        return amount;
    }

    void setAmount(double d) {
        amount = d;
    }

    public String getAmountString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {

        Date d = new Date(date);
        return d.toString();
    }

    public Location getLocation() {
        return location;
    }


}
