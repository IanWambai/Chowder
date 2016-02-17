package com.toe.chowder.sample.activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toe.chowder.Chowder;
import com.toe.chowder.sample.R;

import java.util.ArrayList;

/**
 * Created by Wednesday on 1/20/2016.
 */
public class MainActivity extends AppCompatActivity {

    //Test parameters you can replace these with your own PayBill details
    String MERCHANT_ID = "898998";
    String PASSKEY = "ada798a925b5ec20cc331c1b0048c88186735405ab8d59f968ed4dab89da5515";

    EditText etAmount, etPhoneNumber;
    Button bPay;

    Chowder chowder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setUp();
    }

    private void setUp() {
        etAmount = (EditText) findViewById(R.id.etAmount);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        bPay = (Button) findViewById(R.id.bPay);

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                //Your product's ID must have 13 digits
                String productId = "1717171717171";

                makePayment(productId, amount, phoneNumber);

                //      The Merchant captures the payment details and prepares call to the SAG’s endpoint
                //      The Merchant invokes SAG’s processCheckOut interface
                //      The SAG validates the request sent and returns a response
                //      Merchant receives the processCheckoutResponse parameters namely
                //      TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and CUST_MSG (Customer message)
                //      The merchant is supposed to display the CUST_MSG to the customer after which the merchant should invoke SAG’s confirmPaymentRequest interface to confirm the transaction
                //      The system will push a USSD menu to the customer and prompt the customer to enter their BONGA PIN and any other validation information.
                //      The transaction is processed on M-PESA and a callback is executed after completion of the transaction
            }
        });
    }

    private void makePayment(final String productId, String amount, String phoneNumber) {
        chowder = new Chowder(MainActivity.this, MERCHANT_ID, PASSKEY, amount, phoneNumber.replaceAll("\\+", ""), productId);
        chowder.processPayment();
        chowder.paymentCompleteDialog = new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Check user's SMS inbox for confirmation text
                        //You can also use a callback URL to confirm the transaction, but I'll add that soon
                        getConfirmationText(productId);
                    }
                });

        //      That's it! You can now process payments using the M-Pesa API
        //      IMPORTANT: Any cash you send to the test PayBill number is no-refundable, so use small amounts to test
    }

    private void getConfirmationText(String productId) {
        ArrayList<String> messages = new ArrayList<>();
        final String SMS_URI_INBOX = "content://sms/inbox";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContentResolver().query(uri, projection, "address='SAFARICOM'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    messages.add(strbody);
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }

        boolean hasReceivedText = false;
        if (messages.size() > 0) {
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).contains(productId)) {
                    hasReceivedText = true;
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Confirmation text not found", Toast.LENGTH_LONG).show();
            chowder.paymentCompleteDialog.show();
        }

        if (hasReceivedText) {
            Toast.makeText(getApplicationContext(), "Transaction confirmed", Toast.LENGTH_LONG).show();
            //The user has paid. Do your thing.
        } else {
            Toast.makeText(getApplicationContext(), "Confirmation text not found", Toast.LENGTH_LONG).show();
            chowder.paymentCompleteDialog.show();
        }
    }
}
