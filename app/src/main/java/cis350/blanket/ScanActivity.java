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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Process the scanned data
        Bundle extras = getIntent().getExtras();
        codeResults = (String)extras.get("scanResult");

        setContentView(R.layout.activity_scan);

        dbHandler = new MyDBHandler(this, null, null, 1);

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

        contents = "David";

        Person client = dbHandler.findProduct(contents);

        // If we find a match in the database
        if (client != null) {

            // Getting the giftbasket doesn't seem to work
            //String giftBasket = client.get_giftBasket();

            String giftBasket = "Food,Blanket,Jacket,Water";

            // Separate out by comma
            String [] items = giftBasket.split(",");
            String [] arr = new String[items.length + 1];
            for (int i = 0; i < items.length; i++) {
                arr[i] = items[i];
            }
            arr[items.length] = "General Donation";

            ListView checkList = (ListView) findViewById(R.id.checkBox);
            checkList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            checkList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.text1, arr));

        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = "The code says " + scanResult.getContents();
            Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MenuItemFragment.newInstance(position + 1))
                .commit();
    }
}
