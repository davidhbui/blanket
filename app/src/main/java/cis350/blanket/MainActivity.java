package cis350.blanket;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/*
Main Activity, run when you first open the app

By David Bui, Shiv Patel, Tejas Narayan, and Aashish Lalani
 */


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final int ScanActivity_ID = 1;
    public static final int RelationDonationActivity_ID = 2;

     // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    //  Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    // Holder for all of our cards
    private LinearLayout donationScreen;
    private boolean donationListReady;
    public double totalDonationAmount;
    private DonationAdapter donationAdapter;

    private LinearLayout relationsScreen;
    private boolean relationsListReady;
    private RelationAdapter relationsAdapter;

    static String currUser = null;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    public static Profile currentPro;

    private LinearLayout newsFeedScreen;

    String locationProvider;
    LocationManager locationManager;
    List<Donation> donationList;

    private String[] wishList;
    private String dName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        relationsListReady = false;
        donationListReady = false;

        startActivityForResult(new Intent(this, Startup.class), 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        //while (Profile.getCurrentProfile() == null) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile",
                "user_friends", "email"));

        //}

        // LOOK: This has been causing runtime exceptions on some Android devices
        // but it works perfectly on the emulator.  We were unable to find out why

        // Bui: I'm commenting this out to test it on my phone
        // Tejas: We think it might have to do with how the emulator handles threads vs the devices
//
//        currentPro = Profile.getCurrentProfile();
//        setupFB();
//        String userID = BlanketApplication.getInstance().userID;
//
//        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
//        "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");
//
//        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("SCUser");
//        query1.whereEqualTo("facebookID", userID);
//        query1.getFirstInBackground(new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                if (e == null) {
//                    //User exists, do nothing
//                    Log.d("User does", " exist, do nothing");
//                } else {
//                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
//                        onBoard();
//                    } else {
//                        //unknown error, debug
//                    }
//                }
//            }
//        });

        // This has to be hard coded in to work on my (Bui)'s phone but it works on the emulator
//        StreetChangeApplication.getInstance().userID = "10206253474873718";
//        StreetChangeApplication.getInstance().userName = "Tejas Narayan";

        StreetChangeApplication.getInstance().userID = "1228496193";
        StreetChangeApplication.getInstance().userName = "David Bui";

        // Use the appropriate fragment
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the pull out menu drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationProvider = LocationManager.NETWORK_PROVIDER;

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        // Set up our current location
        Location curr = locationManager.getLastKnownLocation(locationProvider);

        // Default to Philadelphia
        if (curr == null) {
            curr = new Location("Default");
            curr.setLatitude(39.9500);
            curr.setLongitude(-75.1667);
        }
        StreetChangeApplication.getInstance().currentLocation = curr;



        //////////////////////////////////////////////////
        //                                              //
        //      Setting up the Donations Screen         //
        //                                              //
        //////////////////////////////////////////////////

        // Set up the cards for the donation history
        RecyclerView donationListView = (RecyclerView) findViewById(R.id.cardList);
        donationListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        donationListView.setLayoutManager(llm);

        // Sort the donations by date and update the total donation amount
        totalDonationAmount = 0;
        donationList = generateDonations();

        // Update the global variable
        StreetChangeApplication.getInstance().donationList = donationList;
        StreetChangeApplication.getInstance().totalDonationAmount = totalDonationAmount;

        // This determines which type of information will go on the cards
        donationAdapter = new DonationAdapter();
        donationListView.setAdapter(donationAdapter);

        // Start this information off as invisible
        donationScreen = (LinearLayout) findViewById(R.id.donationScreen);

        // Fill in the total donation amount
        TextView donationAmountTextView = (TextView)findViewById(R.id.totalDonation);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String sTotalDonationAmount =  formatter.format(totalDonationAmount);
        donationAmountTextView.append(" " + sTotalDonationAmount);

        donationScreen.setVisibility(View.INVISIBLE);
        donationListReady = true;

        //////////////////////////////////////////////////
        //                                              //
        //      Setting up the Relationships Screen     //
        //                                              //
        //////////////////////////////////////////////////

        // Set up the cards for the donation history
        RecyclerView RelationListView = (RecyclerView) findViewById(R.id.cardList1);
        RelationListView.setHasFixedSize(true);
        LinearLayoutManager llmr = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RelationListView.setLayoutManager(llmr);

        // Sort the donations by date and update the total donation amount
        //totalDonationAmount = 0;
        List<Relation> relationList = new ArrayList<Relation>();
        relationList = generateRelations();
        // Update the global variable
        StreetChangeApplication.getInstance().relationList = relationList;

        // This determines which type of information will go on the cards
        relationsAdapter = new RelationAdapter();
        RelationListView.setAdapter(relationsAdapter);

        // Start this information off as invisible
        relationsScreen = (LinearLayout) findViewById(R.id.relationScreen);

        // Fill in the total donation amount
        TextView relationAmountTextView = (TextView)findViewById(R.id.numRelation);
       // NumberFormat formatter2 = NumberFormat.getCurrencyInstance();
        //String sTotalRelationAmount =  formatter.format(relationList.size());
        relationAmountTextView.append(" " + relationList.size());

        relationsScreen.setVisibility(View.INVISIBLE);
        relationsListReady = true;

        //////////////////////////////////////////////////
        //                                              //
        //      Setting up the NewsFeed Screen          //
        //                                              //
        //////////////////////////////////////////////////

        // Set up the cards for the donation history
        RecyclerView newsFeedListView = (RecyclerView) findViewById(R.id.cardList2);
        newsFeedListView.setHasFixedSize(true);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        newsFeedListView.setLayoutManager(llm2);

        List<Donation> filtered = generateNewsFeed();

        // This determines which type of information will go on the cards
        NewsFeedAdapter newsAdapter = new NewsFeedAdapter(filtered);
        newsFeedListView.setAdapter(newsAdapter);

        newsFeedScreen = (LinearLayout) findViewById(R.id.newsFeedScreen);
        newsFeedScreen.setVisibility(View.VISIBLE);

        ScrollView aboutScreen = (ScrollView)findViewById(R.id.about);
        aboutScreen.setVisibility(View.INVISIBLE);

        // Push notifications

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("ch", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    public void onBoard() {
        startActivityForResult(new Intent(this, Setup.class), 0);
    }

    // Setup facebook profile
    private void setupFB() {
        String userID = currentPro.getId();
        StreetChangeApplication.getInstance().userID = userID;
    }

    @Override
// Handle items being selected in the navigation drawer
   public void onNavigationDrawerItemSelected(int position) {

        // If the donation list exists
        if (donationListReady) {
            donationScreen = (LinearLayout)findViewById(R.id.donationScreen);
            // If we're in the donation history section of the menu
            if (position == 3) {
                donationScreen.setVisibility(View.VISIBLE);
            } else {
                donationScreen.setVisibility(View.INVISIBLE);
            }

            if (position == 0) {
                newsFeedScreen.setVisibility(View.VISIBLE);
            } else {
                newsFeedScreen.setVisibility(View.INVISIBLE);
            }
        }

        if (relationsListReady) {
            relationsScreen = (LinearLayout)findViewById(R.id.relationScreen);
            // If we're in the donation history section of the menu
            if (position == 2) {
                relationsScreen.setVisibility(View.VISIBLE);
            } else {
                relationsScreen.setVisibility(View.INVISIBLE);
            }

            ScrollView aboutScreen = (ScrollView)findViewById(R.id.about);
            if (position == 4) {
                aboutScreen.setVisibility(View.VISIBLE);
            } else {
                aboutScreen.setVisibility(View.INVISIBLE);
            }

        }


        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MenuItemFragment.newInstance(position + 1))
                .commit();
    }

    // Set up the pull-out menu drawer
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    // What happens when you press the scan button (in the Scan Code menu item)
    public void onScanButtonClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientation(1);
        integrator.initiateScan();
    }
    public void onRelationDonationClick(View v) {
        Intent i = new Intent(this, RelationDonationActivity.class);
        startActivityForResult(i, RelationDonationActivity_ID);
    }

    // After we return from the QR Code scan
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {

            Intent i = new Intent(this, ScanActivity.class);
            i.putExtra("scanResult", scanResult.getContents());
            startActivityForResult(i, ScanActivity_ID);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // Action bar (top of the screen)
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Populate relation list based on donation list
    private List<Relation> generateRelations() {

        List<Relation> relationList = new ArrayList<Relation>();
        Map<String,Double> donationAmount = new HashMap<String, Double>();
        Map<String,Integer> donationCounts = new HashMap<String, Integer>();

        // Look for the unique recipients that show up in the list of donations
        for (Donation d : StreetChangeApplication.getInstance().donationList) {

            String recipient = d.getRecipientID();

            double totalAmount = 0;
            int totalDonations = 0;
            if (donationAmount.containsKey(recipient)) {
                totalAmount = donationAmount.get(recipient);
                totalDonations = donationCounts.get(recipient);
            }
            totalDonations++;
            totalAmount += d.getAmount();
            donationAmount.put(recipient, totalAmount);
            donationCounts.put(recipient, totalDonations);

        }

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        for (String id : donationAmount.keySet()) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");

            try {
                ParseObject person = query.get(id);
                dName = (String)person.get("Name");
                wishList = (String[])person.get("wishlist");
            } catch (Exception e) {
            }

            Person p = new Person(id, dName, 2, wishList);
            int count = donationCounts.get(id);
            double amount = donationAmount.get(id);
            Relation rel = new Relation(p, count, amount);
            relationList.add(rel);

        }

        return relationList;
    }

    // Get the list of donations from the database
    private List<Donation> generateDonations() {

        // Our list to return
        List<Donation> unsortedDonationList = new ArrayList<Donation>();

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        String userID = StreetChangeApplication.getInstance().userID;
        String userName = StreetChangeApplication.getInstance().userName;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
        query.whereEqualTo("userID", userID);

        final Context c = getApplicationContext();
        Geocoder geocoder = new Geocoder(c);
        try {
            List<ParseObject> qDonationList = query.find();

            for (ParseObject donationObject : qDonationList) {

                String item = donationObject.getString("item");
                String recipID = donationObject.getString("recipientID");
                String recipName = donationObject.getString("recipientName");
                double amount = donationObject.getDouble("amount");
                int isPush = donationObject.getInt("isPush");
                double dLatitude = donationObject.getDouble("latitude");
                double dLongitude = donationObject.getDouble("longitude");

                Location loc = new Location("East Greenwich, RI");
                loc.setLatitude(dLatitude);
                loc.setLongitude(dLongitude);

                try {
                    List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 5);
                    Address address = addresses.get(0);
                    loc.setProvider(address.getLocality());
                } catch (IOException io) {

                }

                Date donationDate = donationObject.getCreatedAt();
                long dateTime = donationDate.getTime();

                Donation newDonation = new Donation(recipID, item, dateTime, amount, loc);

                newDonation.setRedeemed(isPush == 1);
                newDonation.setRecipientName(recipName);
                newDonation.setDonorName(userName);

                totalDonationAmount += amount;

                unsortedDonationList.add(newDonation);

            }
            return sortList(unsortedDonationList);

        } catch (ParseException e) {
        }

        return unsortedDonationList;

    }

    // Sort the list of donations
    public List<Donation> sortList(List<Donation> unsortedList) {

        // Our list to return
        List<Donation> sortedList = new ArrayList<Donation>();

        HashMap<Long, Donation> dateDonationMap = new HashMap<Long, Donation>();
        List<Long> dateList = new ArrayList<Long>();

        for (Donation donation : unsortedList) {

            long longDate = donation.getDate();
            dateList.add(longDate);
            dateDonationMap.put(longDate, donation);
        }

        // Sort the dates
        Collections.sort(dateList, Collections.reverseOrder());

        // Re-enter them into the list
        for (long longDate : dateList) {
            Donation donation = dateDonationMap.get(longDate);
            sortedList.add(donation);
        }

        return sortedList;
    }

    // Get nearby donations to add to the news feed
    public List<Donation> generateNewsFeed () {

        Location curr = StreetChangeApplication.getInstance().currentLocation;

        double currentLatitude = curr.getLatitude();
        double currentLongitude = curr.getLongitude();

        // Our list to return
        List<Donation> nearbyDonations = new ArrayList<Donation>();

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        String userID = StreetChangeApplication.getInstance().userID;

        // Separate queries for the 4 quadrants around our location
        ParseQuery<ParseObject> posLat_posLong_Query = ParseQuery.getQuery("Donation");
        ParseQuery<ParseObject> posLat_negLong_Query = ParseQuery.getQuery("Donation");
        ParseQuery<ParseObject> negLat_posLong_Query = ParseQuery.getQuery("Donation");
        ParseQuery<ParseObject> negLat_negLong_Query = ParseQuery.getQuery("Donation");

        // Only include donations I haven't made
        posLat_posLong_Query.whereNotEqualTo("userID", userID);
        posLat_negLong_Query.whereNotEqualTo("userID", userID);
        negLat_posLong_Query.whereNotEqualTo("userID", userID);
        negLat_posLong_Query.whereNotEqualTo("userID", userID);

        posLat_posLong_Query.setLimit(10);
        posLat_negLong_Query.setLimit(10);
        negLat_posLong_Query.setLimit(10);
        negLat_negLong_Query.setLimit(10);

        // Latitude bounds
        posLat_posLong_Query.whereLessThanOrEqualTo("latitude", currentLatitude + 0.5);
        posLat_negLong_Query.whereLessThanOrEqualTo("latitude", currentLatitude + 0.5);
        negLat_posLong_Query.whereLessThanOrEqualTo("latitude", currentLatitude - 0.5);
        negLat_negLong_Query.whereLessThanOrEqualTo("latitude", currentLatitude - 0.5);

        // Longitude bounds
        posLat_posLong_Query.whereLessThanOrEqualTo("longitude", currentLongitude + 0.5);
        negLat_posLong_Query.whereLessThanOrEqualTo("longitude", currentLongitude + 0.5);
        posLat_negLong_Query.whereLessThanOrEqualTo("longitude", currentLongitude - 0.5);
        negLat_negLong_Query.whereLessThanOrEqualTo("longitude", currentLongitude - 0.5);

        final Context c = getApplicationContext();
        Geocoder geocoder = new Geocoder(c);

        try {

            List<ParseObject> ppDonationList = posLat_posLong_Query.find();
            List<ParseObject> pnDonationList = posLat_negLong_Query.find();
            List<ParseObject> npDonationList = negLat_posLong_Query.find();
            List<ParseObject> nnDonationList = negLat_negLong_Query.find();

            Set<ParseObject> masterSet = new HashSet<ParseObject>();
            masterSet.addAll(ppDonationList);
            masterSet.addAll(pnDonationList);
            masterSet.addAll(npDonationList);
            masterSet.addAll(nnDonationList);

            int counter = 0;
            for (ParseObject donationObject : masterSet) {

                counter++;

                String item = donationObject.getString("item");
                String recipID = donationObject.getString("recipientID");
                String recipName = donationObject.getString("recipientName");
                String donorName = donationObject.getString("userName");
                double dLatitude = donationObject.getDouble("latitude");
                double dLongitude = donationObject.getDouble("longitude");
                double amount = donationObject.getDouble("amount");

                Location geocode = new Location("Bel Air, California");
                geocode.setLatitude(dLatitude);
                geocode.setLongitude(dLongitude);
                try {
                    List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
                    Address address = addresses.get(0);
                    geocode.setProvider(address.getLocality());
                } catch (IOException io) {

                }

                Date donationDate = donationObject.getCreatedAt();
                long dateTime = donationDate.getTime();

                Donation newDonation = new Donation(recipID, item, dateTime, amount, geocode);
                newDonation.setRecipientName(recipName);
                newDonation.setDonorName(donorName);

                nearbyDonations.add(newDonation);

                // Stop processing donations if we have 10 nearby ones
                if (counter > 10) {
                    break;
                }

            }
            return sortList(nearbyDonations);

        } catch (ParseException e) {
        }

        return nearbyDonations;

    }
}
