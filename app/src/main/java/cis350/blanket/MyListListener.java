package cis350.blanket;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by davidhb on 4/3/2015.
 */
public class MyListListener implements AdapterView.OnItemSelectedListener {

    public int choice;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String choiceString = parent.getItemAtPosition(position).toString();
        choice = Integer.parseInt(choiceString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
