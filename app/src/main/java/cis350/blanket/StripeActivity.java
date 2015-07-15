package cis350.blanket;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.exception.AuthenticationException;
import com.stripe.android.model.Token;

import java.util.HashMap;
import java.util.Map;

public class StripeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe);
        Parse.initialize(this, "WhyDX5it2l6Tg3EgQUuX8m9XxlqaT2Fh0XFiNgdq",
                "SkUYkfWplylFJYPY4E199xUstPqvNk00PScCFxRX");

        Card card = new Card("4242424242424242", 12, 2016, "123");

        card.validateNumber();
        card.validateCVC();

        Stripe stripe = null;
        try {
            stripe = new Stripe("pk_test_1phL7e7ejalPKqyEOTDHZTKi");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        HashMap<String, String> params = new HashMap<String, String>();
                        //params.put("token", token);
                        params.put("tokenID", token.getId());

                        System.out.println("******" + token.getId());
                        ParseCloud.callFunctionInBackground("test", params, new FunctionCallback<String>() {
                            public void done(String object, ParseException e) {
                                if (e == null) {
                                    //Toast.makeText(getApplicationContext(), object.get("answer").toString(),
                                    //      Toast.LENGTH_LONG).show();
                                    //System.out.println(object.get("answer").toString());

                                }
                                else {
                                    e.printStackTrace();
                                }

                            }
                        });

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





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stripe, menu);
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