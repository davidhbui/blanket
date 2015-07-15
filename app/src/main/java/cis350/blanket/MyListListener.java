package cis350.blanket;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Returns the index of an item selected from a checklist, or the item itself in String form
 *
 * Created by davidhb on 4/3/2015.
 */
public class MyListListener implements AdapterView.OnItemClickListener {

    public int choice = 10;
    public String choiceString = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        choiceString = parent.getItemAtPosition(position).toString();
        choice = position;

    }

}
