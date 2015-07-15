package cis350.blanket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by davidhb on 5/1/2015.
 * Activity that starts when we choose to donate via existing relation
 */
public class RelationDonationActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final int ScanActivity_ID = 4;

    // List of the people
    private ListView personList;
    String[] people;
    String[] ids;
    String selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_relation_donation);

        // Get all of the relations out
        List<Relation> relations = StreetChangeApplication.getInstance().relationList;

        people = new String[relations.size()];
        ids = new String[relations.size()];

        for (int i = 0; i < relations.size(); i++) {
            Relation relation = relations.get(i);
            Person recipient = relation.getRecipient();

            people[i] = recipient.getProductName();
            ids[i] = recipient.getID();
        }

        personList = (ListView) findViewById(R.id.recip_list);
        personList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        personList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.text1, people));
        personList.setOnItemClickListener(new MyListListener());

    }

    public void onRecipButtonClick(View v) {

        // Pass the selection in to pull up the appropriate wishlist
        MyListListener listener = (MyListListener)personList.getOnItemClickListener();
        int index = listener.choice;
        selection = ids[index];

        Intent i = new Intent(this, ScanActivity.class);

        i.putExtra("scanResult", selection);
        startActivityForResult(i, ScanActivity_ID);

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
