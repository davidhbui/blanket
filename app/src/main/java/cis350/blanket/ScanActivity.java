package cis350.blanket;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/*
Begin to process a donation by presenting the user with the homeless person's wishlist

By David Bui
 */


public class ScanActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String codeResults;         // ID
    private String selection;
    public static final int DonationAmount_ID = 4;
    private ListView checkList;
    private String[] arr;
    private ArrayList<String> items;
    private String rName;
    private boolean fill;
    private int itemsLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Process the scanned data
        Bundle extras = getIntent().getExtras();
        codeResults = (String)extras.get("scanResult");

        setContentView(R.layout.activity_scan);

        selection = "";
        fill = false;


        // If we have a string to look at, process it
        if (codeResults.compareTo("") != 0) {
            process();
        }

        itemsLength = 0;


    }

    // What happens when you press the scan button
    public void process() {

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");


        final Context c = getApplicationContext();

        // Get the person in the database based on the QR code
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");

        try {

            ParseObject person = query.get(codeResults);
            rName = (String)person.get("Name");
            items = (ArrayList<String>)person.get("wishlist");
            itemsLength = items.size();
        } catch (Exception e) {

        }


        arr = new String[itemsLength + 1];
        for (int i = 0; i < itemsLength; i++) {
            arr[i] = items.get(i);
        }
        arr[itemsLength] = "General Donation";

        // Populate the wishlist screen
        fill = true;
        checkList = (ListView) findViewById(R.id.checkBox);
        checkList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        checkList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.text1, arr));
        checkList.setOnItemClickListener(new MyListListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }
    @Override
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


    public void onScanButtonClick(View v) {

        if (fill == true) {

            // Get the item that the user selected
            MyListListener listener = (MyListListener)checkList.getOnItemClickListener();
            selection = listener.choiceString;

            // Pass all of this into the donation amount screen
            Intent i = new Intent(this, DonationAmount.class);
            i.putExtra("recipientName", rName);
            i.putExtra("donation_id", codeResults);
            i.putExtra("item", selection);
            startActivityForResult(i, DonationAmount_ID);
        } else {
            Toast.makeText(getApplicationContext(), "Make a selection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MenuItemFragment.newInstance(position + 1))
                .commit();
    }
}
