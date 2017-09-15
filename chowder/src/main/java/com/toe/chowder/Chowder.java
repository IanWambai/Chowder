package com.toe.chowder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.toe.chowder.utils.ChowderUtils.encodeBase64;
import static com.toe.chowder.utils.ChowderUtils.showMessage;


/**
 * Created by Wednesday on 1/16/2016.
 */
public class Chowder {

    private String BASE_URL_SANDBOX = "https://sandbox.safaricom.co.ke/mpesa/";
    private String BASE_URL_LIVE = "https://sandbox.safaricom.co.ke/mpesa/";

    private String MPESA_KEY = "wqsTcAtuGr23sSw8bGJ2wiH2kuKsoiRb";
    private String MPESA_SECRET = "agIRcwcVIGHd6ozD";

    private String API_TYPE_OAUTH = "oauth/";
    private String API_TYPE_C2B = "c2b/";
    private String API_TYPE_B2C = "b2c/";
    private String API_TYPE_B2B = "b2b/";
    private String API_TYPE_ACCOUNT_BALANCE = "accountbalance/";
    private String API_TYPE_STK_PUSH = "stkpush/";
    private String API_TYPE_STK_PUSH_QUERY = "stkpushquery/";
    private String API_TYPE_TRANSACTION_STATUS = "transactionstatus/";

    private String API_VERSION = "v1/";

    private String END_POINT_GENERATE_TOKEN = "generate?grant_type=client_credentials";
    private String END_POINT_PAYMENT_REQUEST = "paymentrequest";
    private String END_POINT_REGISTER_URL = "registerurl";
    private String END_POINT_SIMULATE = "simulate";
    private String END_POINT_QUERY = "query";
    private String END_POINT_REVERSAL = "reversal";
    private String END_POINT_LIPA_NA_MPESA = "processrequest";

    private RequestQueue queue;

    //Others
    private Activity activity;
    private ProgressDialog progress;

    public Chowder(Activity activity) {
        this.activity = activity;
        queue = Volley.newRequestQueue(activity);
    }

    public void generateAccessToken() {
        String URL = BASE_URL_SANDBOX + API_TYPE_OAUTH + API_VERSION + END_POINT_GENERATE_TOKEN;

        Log.d("YAYA", URL);

        try {
            progress = ProgressDialog.show(activity, "Please wait", "Processing payment...", true);

            JSONObject json = new JSONObject();
            json.put("Authorization", encodeBase64(MPESA_KEY, MPESA_SECRET));

            Log.d("YAYA", json.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("YAYA", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showMessage(activity, "Something went wrong. Please try again.");
                    error.printStackTrace();
                    progress.dismiss();
                }
            });

            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            showMessage(activity, "Something went wrong. Please try again.");
        }
    }
}