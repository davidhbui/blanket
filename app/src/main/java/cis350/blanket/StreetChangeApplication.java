package cis350.blanket;

import android.app.Application;
import android.location.Location;

import java.util.List;

/**
 * Created by davidhb on 4/15/2015.
 * A place for all of our global variables, such as the list of donations
 */
public class StreetChangeApplication extends Application {

    public static StreetChangeApplication singleton;

    public List<Donation> donationList;
    public double totalDonationAmount;
    public List<Relation> relationList;
    public String passcode;
    public String userID;
    public String userName;

    public Location currentLocation;

    public static StreetChangeApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

}
