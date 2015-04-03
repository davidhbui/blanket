package cis350.blanket;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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
        submit.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                String string = input.getText().toString();
                if (string.equals("answer")) {
                    Intent myIntent = new Intent(context, PaymentConfirmation.class);
                    myIntent.putExtra("item", previous.getStringExtra("item"));

                    startActivity(myIntent);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect Password";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    input.clearComposingText();
                }

                //Intent myIntent = new Intent(context, NextScreen.class);
                //myIntent.putExtra("pass", pass.getText().toString());
                //myIntent.putExtra("phone", phone.getText().toString());
                //startActivity(myIntent);
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
