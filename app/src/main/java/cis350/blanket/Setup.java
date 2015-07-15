/*
Author: Tejas Narayan
This is the onboarding screen that is called if the user has not already been registered on the system.
 */
package cis350.blanket;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;

import com.parse.ParseObject;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

public class Setup extends ActionBarActivity {

    ParseObject newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
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

    /**
     * Stores all of the data input in the fields into the Parse database
     */
    public void onSubmitButtonClick(View view){
        EditText PINIn = (EditText)findViewById(R.id.PINText);
        EditText CreditIn = (EditText)findViewById(R.id.number);
        EditText DonationIn = (EditText)findViewById(R.id.DefaultAmount);
        EditText MonthIn = (EditText) findViewById(R.id.month);
        EditText YearIn = (EditText) findViewById(R.id.year);
        EditText CVCIn = (EditText) findViewById(R.id.cvc);

        String PIN = PINIn.getText().toString();
        String Credit = CreditIn.getText().toString();
        int Month = Integer.parseInt(MonthIn.getText().toString());
        int Year = Integer.parseInt(YearIn.getText().toString());
        String CVC = CVCIn.getText().toString();
        double Default = Double.parseDouble(DonationIn.getText().toString());

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        Card card = new Card(Credit, Month, Year, CVC);

        card.validateNumber();
        card.validateCVC();

        newUser = new ParseObject("SCUser");
        newUser.put("facebookID", StreetChangeApplication.getInstance().userID);

        Stripe stripe = null;
        try {
            stripe = new Stripe("pk_test_1phL7e7ejalPKqyEOTDHZTKi");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        /**
         * Creates the stripe token to be used for transactions, cannot successfully store this
         */
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        String saveThisInformation = token.getId();
                        saveToken(saveThisInformation);
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(getApplicationContext(),
                                "Error",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        newUser.put("password", PIN);
        newUser.put("defaultDonation", Default);
        newUser.saveInBackground();

        Toast.makeText(getApplicationContext(), "Thank You, you have been registered",
                Toast.LENGTH_LONG).show();

        //Finish the setup activity
        finish();
    }

    public void saveToken(String s) {
        newUser.put("tokenID", s);
    }
}
