package com.toe.chowder.sample.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toe.chowder.ChowderSoap;
import com.toe.chowder.data.Subscription;
import com.toe.chowder.interfaces.PaymentListener;
import com.toe.chowder.sample.R;

import java.util.ArrayList;

/**
 * Created by Wednesday on 1/20/2016.
 */
public class ChowderSoapSample extends AppCompatActivity implements PaymentListener {

    //Test parameters you can replace these with your own PayBill details
    private String PAYBILL_NUMBER = "898998";
    private String PASSKEY = "ada798a925b5ec20cc331c1b0048c88186735405ab8d59f968ed4dab89da5515";

    //Views
    private EditText etAmount, etPhoneNumber;
    private Button bPay, bConfirm;

    //Chowder
    private ChowderSoap chowder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chowder_soap_sample);

        setUp();
        checkSubscriptions();
    }

    private void setUp() {
        chowder = new ChowderSoap(ChowderSoapSample.this, PAYBILL_NUMBER, PASSKEY, this);

        etAmount = (EditText) findViewById(R.id.etAmount);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        bPay = (Button) findViewById(R.id.bPay);
        bConfirm = (Button) findViewById(R.id.bConfirm);

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                //Your product's ID must have 13 characters
                String productId = "1717171717171";

                if (amount.length() > 0 && phoneNumber.length() > 0) {
                    chowder.processPayment(amount, phoneNumber.replaceAll("\\+", ""), productId);
                }

                //      That's it! You can now process payments using the M-Pesa API
                //      IMPORTANT: Any cash you send to the test PayBill number is non-refundable, so use small amounts to test


                //      What's happening:
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
                confirmLastPayment();
            }
        });
    }

    private void confirmLastPayment() {
        SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //We saved the last transaction id to Shared Preferences
        String transactionId = sp.getString("chowderTransactionId", null);

        //Call chowder.checkTransactionStatus to check a transaction
        //Check last transaction
        if (transactionId != null) {
            chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionId);
        } else {
            Toast.makeText(getApplicationContext(), "No previous transaction available", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSubscriptions() {
        //This is how you check whether a single product's subscription is valid
        String productId = "1717171717171";
        boolean isSubscribed = chowder.checkSubscription(productId);

        //This is how you retrieve all the product's subscriptions
        ArrayList<Subscription> validatedSubscriptions = chowder.checkAllSubscriptions();

        //This how you check a subscribed product's data from the list
        if (validatedSubscriptions.size() > 0) {
            Subscription subcribedProduct = validatedSubscriptions.get(0);
            String subscribedProductId = subcribedProduct.getProductId();
            boolean isSubscriptionValid = subcribedProduct.isSubscriptionValid();

            //You get the product Id and whether or not it's subscription is valid
        }
    }

    @Override
    public void onPaymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId) {
        //The user is now waiting to enter their PIN
        //You can use the transaction id to confirm payment to make sure you store the ids somewhere if you want the user to be able to check later
        //Save the transaction ID
        SharedPreferences sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sp.edit().putString("chowderTransactionId", transactionId).apply();

        new AlertDialog.Builder(ChowderSoapSample.this)
                .setTitle("Payment in progress")
                .setMessage("Please wait for a pop up from Safaricom and enter your Bonga PIN.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Well you can skip the dialog if you want, but it will make the user feel safer, they'll know what's going on instead of sitting there
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onPaymentSuccess(String merchantId, String msisdn, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        //When the payment is complete you have the option to subscribe the user
        //This means that after a set period of time you can prompt the user to make another payment if the subscription is invalid
        String productId = "1717171717171";
        chowder.subscribeForProduct(productId, ChowderSoap.SUBSCRIBE_DAILY);

        //After a month, if you check the product's subscription it will be invalid, but before it will be valid

        //The payment was successful.
        new AlertDialog.Builder(ChowderSoapSample.this)
                .setTitle("Payment confirmed")
                .setMessage(transactionStatus + ". Your amount of Ksh." + amount + " has been successfully paid from " + msisdn + " to PayBill number " + merchantId + " with the M-Pesa transaction code " + mpesaTransactionId + " on " + mpesaTransactionDate + ".\n\nThank you for your business.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Well you can skip the dialog if you want, but it might make the user feel safer
                        //The user has successfully paid so give them their goodies
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onPaymentFailure(String merchantId, String msisdn, String amount, String transactionStatus, String processDescription) {
        //The payment has failed.
        new AlertDialog.Builder(ChowderSoapSample.this)
                .setTitle("Payment failed")
                .setMessage(transactionStatus + ". Your amount of Ksh." + amount + " was not paid from " + msisdn + " to PayBill number " + merchantId + ". Please try again.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amount = etAmount.getText().toString().trim();
                        String phoneNumber = etPhoneNumber.getText().toString().trim();
                        //Your product's ID must have 13 digits
                        String productId = "1717171717171";

                        chowder.processPayment(amount, phoneNumber.replaceAll("\\+", ""), productId);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Well you can skip the dialog if you want, but it might make the user feel safer
                //The user has successfully paid so give them their goodies
                dialog.dismiss();
            }
        }).show();
    }
}
