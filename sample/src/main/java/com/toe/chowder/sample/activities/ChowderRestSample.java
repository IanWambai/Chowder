package com.toe.chowder.sample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.toe.chowder.Chowder;
import com.toe.chowder.sample.R;

/**
 * Created by Wednesday on 1/20/2016.
 */
public class ChowderRestSample extends AppCompatActivity {

    //Views
    EditText etAmount, etPhoneNumber;
    Button bPay, bConfirm;

    //Chowder
    Chowder chowder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chowder_rest_sample);

        setUp();
    }

    private void setUp() {
        chowder = new Chowder(this);

        etAmount = (EditText) findViewById(R.id.etAmount);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        bPay = (Button) findViewById(R.id.bPay);
        bConfirm = (Button) findViewById(R.id.bConfirm);


        chowder.generateAccessToken();
    }
}