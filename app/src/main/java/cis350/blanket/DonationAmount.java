package cis350.blanket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by shivpatel on 4/16/15.
 */
public class DonationAmount extends ActionBarActivity {

    private EditText input;
    private Button submit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_amount);

        context = getApplicationContext();
        input = (EditText) findViewById(R.id.editText);

        submit = (Button) findViewById(R.id.button);

        input.setGravity(Gravity.CENTER_HORIZONTAL);
        final Intent previous = getIntent();

        submit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent myIntent = new Intent(context, PasscodeScreen.class);
                myIntent.putExtra("recipientName", previous.getStringExtra("recipientName"));

                myIntent.putExtra("donationAmount", input.getText().toString());
                myIntent.putExtra("item", previous.getStringExtra("item"));
                myIntent.putExtra("donation_id", previous.getStringExtra("donation_id"));
                startActivity(myIntent);
            }
        });

        double defaultPay = 5.00;

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("SCUser");
            query.whereEqualTo("facebookID", StreetChangeApplication.getInstance().userID);
            List<ParseObject> temp = query.find();
            ParseObject temp1 = temp.get(0);
            defaultPay = Double.parseDouble((temp1.get("defaultDonation").toString()));

        } catch (com.parse.ParseException e) {

        }

        input.setText(Double.toString(defaultPay));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    input.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));

                    current = formatted;
                    input.setText(formatted);
                    input.setSelection(formatted.length());
                    input.addTextChangedListener(this);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donation_amount, menu);
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
}
