package cis350.blanket;

import java.sql.Date;
import java.text.NumberFormat;

/**
 * Created by shivpatel on 4/16/15.
 */
public class Relation {

    // Number of times donated
    private int freq;

    // Amount donated
    private double amount;

    // Recipient of donation
    private Person recipient;


    public Relation(Person recipient, int freq, double amount) {
        this.freq = freq;
        this.amount = amount;
        this.recipient = recipient;
    }

    public Person getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    public int getFreq() { return freq; }

}