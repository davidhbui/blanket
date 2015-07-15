package cis350.blanket;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shivpatel on 4/16/15.
 */

public class PasscodeScreen extends ActionBarActivity {

    private EditText input;
    private Button submit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_screen);

        context = getApplicationContext();
        input = (EditText) findViewById(R.id.editText2);
        submit = (Button) findViewById(R.id.button3);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        final Intent previous = getIntent();

        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");


        submit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                String string = input.getText().toString();
                String actualPassword = "";

                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("SCUser");
                    query.whereEqualTo("facebookID", StreetChangeApplication.getInstance().userID);
                    List<ParseObject> temp = query.find();
                    ParseObject temp1 = temp.get(0);
                    actualPassword = (String) temp1.get("password");

                } catch (com.parse.ParseException e) {

                }
                if (string.equals(actualPassword)) {

                    // If we are here then we have made a successful donation
                    String item = previous.getStringExtra("item");
                    String recipientID = previous.getStringExtra("donation_id");
                    String recipientName = previous.getStringExtra("recipientName");

                    String rawDollar = previous.getStringExtra("donationAmount");
                    String cleaned = rawDollar.replaceAll("[$,]", "");

                    double amount = Double.parseDouble(cleaned);
                    Location currLoc = StreetChangeApplication.getInstance().currentLocation;
                    double currentLatitude = currLoc.getLatitude();
                    double currentLongitude = currLoc.getLongitude();

                    ParseObject donationObject = new ParseObject("Donation");
                    donationObject.put("userID", StreetChangeApplication.getInstance().userID);
                    donationObject.put("userName", StreetChangeApplication.getInstance().userName);
                    donationObject.put("recipientID", recipientID);
                    donationObject.put("recipientName", recipientName);
                    donationObject.put("amount", amount);
                    donationObject.put("item", item);
                    donationObject.put("isPush", 0);
                    donationObject.put("latitude", currentLatitude);
                    donationObject.put("longitude", currentLongitude);
                    donationObject.saveInBackground();

                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Donation");
                    query1.whereEqualTo("recepientID", recipientID);

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Person");
                    query2.whereEqualTo("recepientID", recipientID);

                    try {
                        ParseObject recipient = query2.getFirst();
                        String number = recipient.getString("phoneNumber");

                        List<ParseObject> qDonationList = query1.find();

                        for (ParseObject d : qDonationList) {
                            if (d.getBoolean("isPush")) {
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(number, null, "An item you have requested through StreetChange is ready for pickup", null, null);
                            }
                        }

                    } catch (ParseException e){
                    }

                    Intent myIntent = new Intent(context, PaymentConfirmation.class);
                    myIntent.putExtra("item", previous.getStringExtra("item"));

                    HashMap<String, Object> params = new HashMap<String, Object>();

                    String token = "somethingFromDB";

                    params.put("tokenID", token);
                    params.put("amount", previous.getStringExtra("donationAmount"));

                    ParseCloud.callFunctionInBackground("chargeCard", params, new FunctionCallback<String>() {
                        public void done(String object, ParseException e) {
                            if (e == null) {
                                // it worked
                            } else {
                                e.printStackTrace();
                            }

                        }
                    });



                    startActivity(myIntent);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect Password";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    input.clearComposingText();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passcode_screen, menu);
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
