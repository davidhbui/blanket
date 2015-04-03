package cis350.blanket;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cis350.blanket.MainActivity;
import cis350.blanket.R;

public class Setup extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        TextView welcomeDisplay = (TextView)findViewById(R.id.textView);
        welcomeDisplay.setText("Welcome \nPlease enter the information requested below to register!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
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

    public void onSubmitButtonClick(View view){
        Log.d("In oSBC", "It's nice here");
        EditText PINIn = (EditText)findViewById(R.id.PINText);
        EditText CreditIn = (EditText)findViewById(R.id.CreditCardInfo);
        EditText DonationIn = (EditText)findViewById(R.id.DefaultAmount);

        String PIN = PINIn.getText().toString();
        String Credit = CreditIn.getText().toString();
        String Default = DonationIn.getText().toString();

        //Store it in the database here

        Toast.makeText(getApplicationContext(), "Thank You, you have been registered",
                Toast.LENGTH_LONG).show();

        //Finish the setup activity
        finish();
    }
}
