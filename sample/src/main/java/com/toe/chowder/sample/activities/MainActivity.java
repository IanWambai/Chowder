package com.toe.chowder.sample.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.toe.chowder.Chowder;
import com.toe.chowder.R;

/**
 * Created by Wednesday on 1/20/2016.
 */
public class MainActivity extends AppCompatActivity {

    //Test parameters you can replace these with your own PayBill details
    String MERCHANT_ID = "898998";
    String PASSKEY = "ada798a925b5ec20cc331c1b0048c88186735405ab8d59f968ed4dab89da5515";

    EditText etAmount, etPhoneNumber;
    Button bPay;

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

    private void makePayment(String productId, String amount, String phoneNumber) {
        Chowder chowder = new Chowder(MainActivity.this, MERCHANT_ID, PASSKEY);
        chowder.processPayment(productId, amount, phoneNumber);
        chowder.paymentCompleteDialog = new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Do something because the user has completed payment
                        Toast.makeText(getApplicationContext(), "User has successfully paid. You may now provide them with your product or service", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

        //      That's it! You can now process payments using the M-Pesa API
        //      IMPORTANT: Any cash you send to the test PayBill number is no-refundable, so use small amounts to test
    }
}
