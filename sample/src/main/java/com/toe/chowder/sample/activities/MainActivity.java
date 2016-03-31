package com.toe.chowder.sample.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toe.chowder.Chowder;
import com.toe.chowder.sample.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Wednesday on 1/20/2016.
 */
public class MainActivity extends AppCompatActivity {

    //Test parameters you can replace these with your own PayBill details
//    String PAYBILL_NUMBER = "898998";
//    String PASSKEY = "ada798a925b5ec20cc331c1b0048c88186735405ab8d59f968ed4dab89da5515";

    String PAYBILL_NUMBER = "105775";
    String PASSKEY = "1c2640251684e711688d791ac35fa1b8ee9e060943219cb3ff1d15017c8697de";

    EditText etAmount, etPhoneNumber;
    Button bPay, bConfirm;

    Chowder chowder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setUp();
    }

    private void setUp() {
        chowder = new Chowder(MainActivity.this, PAYBILL_NUMBER, PASSKEY);

        etAmount = (EditText) findViewById(R.id.etAmount);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        bPay = (Button) findViewById(R.id.bPay);
        bConfirm = (Button) findViewById(R.id.bConfirm);

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

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                Set<String> transactionIdSet = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    //All the transaction Ids of the transactions are saved as a Set in Shared Preferences
                    transactionIdSet = sp.getStringSet("chowderTransactionIds", null);
                    if (transactionIdSet != null) {
                        ArrayList<String> transactionIds = new ArrayList<>();
                        transactionIds.addAll(transactionIdSet);

                        //Call chowder.checkTransactionStatus to check the transaction
                        chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionIds.get(transactionIds.size() - 1));
                    } else {
                        Toast.makeText(getApplicationContext(), "No transactions found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void makePayment(final String productId, String amount, String phoneNumber) {
        chowder.processPayment(amount, phoneNumber.replaceAll("\\+", ""), productId);
        chowder.paymentCompleteDialog = new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Confirm if the user has made the payment

                        //You use a callback URL to confirm the transaction.
                        // Currently Chowder hosts everything meaning you don't have to
                        // provide a URL or set up any server stuff
                        SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                        Set<String> transactionIdSet = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                            //All the transaction Ids of the transactions are saved as a Set in Shared Preferences
                            transactionIdSet = sp.getStringSet("chowderTransactionIds", null);
                            if (transactionIdSet != null) {
                                ArrayList<String> transactionIds = new ArrayList<>();
                                transactionIds.addAll(transactionIdSet);

                                //Call chowder.checkTransactionStatus
                                chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionIds.get(transactionIds.size() - 1));
                            } else {
                                Toast.makeText(getApplicationContext(), "No transactions found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        //      That's it! You can now process payments using the M-Pesa API
        //      IMPORTANT: Any cash you send to the test PayBill number is non-refundable, so use small amounts to test
    }
}
