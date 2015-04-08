package cis350.blanket;

import android.content.Intent;
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

import java.util.LinkedHashMap;

// Activity to scan a QR code
public class ScanActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String codeResults;
    private MyDBHandler dbHandler;
    private String selection;
    public static final int DonationAmount_ID = 4;
    private ListView checkList;
    private String[] arr;
    private boolean fill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Process the scanned data
        Bundle extras = getIntent().getExtras();
        codeResults = (String)extras.get("scanResult");

        setContentView(R.layout.activity_scan);

        dbHandler = new MyDBHandler(this, null, null, 1);

        selection = "";
        fill = false;

        // If we have a string to look at, process it
        if (codeResults.compareTo("") != 0) {
            process();
        }

    }

    // What happens when you press the scan button
    public void process() {

        String contents = codeResults;
        //Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();

        // Make a dummy (extremely handsome) person
        Person david = new Person(1, "David", 0, "Food,Blanket,Jacket,Water");
        dbHandler.addPerson(david);

        // Hard coded this in so that the demo would work with any QR code that it reads
        //contents = "David";

        // Get the person from the DB
        Person client = dbHandler.findProduct(contents);

        // If we find a match in the database
        if (client != null) {

            // Getting the giftbasket doesn't seem to work
            String giftBasket = client.get_giftBasket();

            // Hard coded this in because getting the gift basket didn't seem to work
            //String giftBasket = "Food,Blanket,Jacket,Water";

            // Separate out by comma
            String [] items = giftBasket.split(",");
            arr = new String[items.length + 1];
            for (int i = 0; i < items.length; i++) {
                arr[i] = items[i];
            }
            arr[items.length] = "General Donation";

            fill = true;
            checkList = (ListView) findViewById(R.id.checkBox);
            checkList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            checkList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.text1, arr));
            checkList.setOnItemSelectedListener(new MyListListener());

        }

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

            MyListListener listener = (MyListListener)checkList.getOnItemSelectedListener();
            int index = listener.choice;
            selection = arr[index];

            Intent i = new Intent(this, DonationAmount.class);
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
