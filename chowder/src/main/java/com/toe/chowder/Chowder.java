package com.toe.chowder;

import android.app.Activity;
import android.app.ProgressDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toe.chowder.constructors.ChowderCredentials;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.toe.chowder.utils.ChowderUtils.encodeBase64;
import static com.toe.chowder.utils.ChowderUtils.showMessage;

public class Chowder {

    //Values
    private String BASE_URL_SANDBOX = "https://sandbox.safaricom.co.ke/mpesa/";
    private String BASE_URL_OAUTH_SANDBOX = "https://sandbox.safaricom.co.ke/";

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

    //Command IDs
    public static String COMMAND_TRANSACTION_REVERSAL = "TransactionReversal";
    public static String COMMAND_SALARY_PAYMENT = "SalaryPayment";
    public static String COMMAND_BUSINESS_PAYMENT = "BusinessPayment";
    public static String COMMAND_PROMOTION_PAYMENT = "PromotionPayment";
    public static String COMMAND_ACCOUNT_BALANCE = "AccountBalance";
    public static String COMMAND_CUSTOMER_PAY_BILL_ONLINE = "CustomerPayBillOnline";
    public static String COMMAND_TRANSACTION_STATUS_QUERY = "TransactionStatusQuery";
    public static String COMMAND_CHECK_IDENTITY = "CheckIdentity";
    public static String COMMAND_BUSINESS_PAY_BILL = "BusinessPayBill";
    public static String COMMAND_BUSINESS_BUY_GOODS = "BusinessBuyGoods";
    public static String COMMAND_DISBURSE_FUNDS_TO_BUSINESS = "DisburseFundsToBusiness";
    public static String COMMAND_BUSINESS_TO_BUSINESS_TRANSFER = "BusinessToBusinessTransfer";
    public static String COMMAND_BUSINESS_TRANSFER_MMF_UTILITY = "BusinessTransferFromMMFToUtility";

    //Identifier Types
    private String TYPE_MSISDN = "1";
    private String TYPE_TILL_NUMBER = "2";
    private String TYPE_SHORT_CODE = "4";

    //Others
    private ChowderCredentials chowderCredentials;
    private Activity activity;
    private RequestQueue queue;

    //Generated Values
    private String accessToken;
    private int expiresIn;

    public Chowder(Activity activity, ChowderCredentials chowderCredentials) {
        this.activity = activity;
        this.chowderCredentials = chowderCredentials;
        queue = Volley.newRequestQueue(activity);
    }

    public void generateAccessToken() {
        String URL = BASE_URL_OAUTH_SANDBOX + API_TYPE_OAUTH + API_VERSION + END_POINT_GENERATE_TOKEN;
        final ProgressDialog progress = ProgressDialog.show(activity, "Please wait", "Generating access token...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            accessToken = responseJson.getString("access_token");
                            expiresIn = Integer.parseInt(responseJson.getString("expires_in"));

                            progress.dismiss();
                        } catch (JSONException e) {
                            showMessage(activity, "Something went wrong: " + e.getMessage());
                            e.printStackTrace();
                        }

                        progress.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(activity, "Something went wrong: " + error.getMessage());
                error.printStackTrace();
                progress.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", encodeBase64(chowderCredentials.getMpesaKey(), chowderCredentials.getMpesaSecret()));
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public void b2cPaymentRequest(String command, int amount) {
        String URL = BASE_URL_SANDBOX + API_TYPE_B2C + API_VERSION + END_POINT_PAYMENT_REQUEST;

        final ProgressDialog progress = ProgressDialog.show(activity, "Please wait", "Making payment request...", true);
        JSONObject json = new JSONObject();
        try {
            json.put("InitiatorName", chowderCredentials.getInitiatorName());
            json.put("SecurityCredential", chowderCredentials.getSecurityCredential());
            json.put("CommandID", command);
            json.put("Amount", amount);
            json.put("PartyA", chowderCredentials.getShortCode1());
            json.put("PartyB", chowderCredentials.getMsisdn());
            json.put("Remarks", "Transaction: " + command);
            json.put("QueueTimeOutURL", "http://test.com/mpesa");
            json.put("ResultURL", "http://test.com/mpesa");
            json.put("Occassion", "Occasion");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String conversationId = response.getString("ConversationID");
                    String originatorConversationID = response.getString("OriginatorConversationID");
                    String responseCode = response.getString("ResponseCode");
                    String responseDescription = response.getString("ResponseDescription");

                    showMessage(activity, responseDescription);

                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(activity, "Something went wrong: " + error.getMessage());
                error.printStackTrace();
                progress.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void b2bPaymentRequest(String command, int amount) {
        String URL = BASE_URL_SANDBOX + API_TYPE_B2B + API_VERSION + END_POINT_PAYMENT_REQUEST;

        final ProgressDialog progress = ProgressDialog.show(activity, "Please wait", "Making payment request...", true);
        JSONObject json = new JSONObject();

        try {
            json.put("CommandID", command);
            json.put("Amount", amount);
            json.put("PartyA", chowderCredentials.getShortCode1());
            json.put("SenderIdentifierType", TYPE_SHORT_CODE);
            json.put("PartyB", chowderCredentials.getShortCode2());
            json.put("RecieverIdentifierType", TYPE_MSISDN);
            json.put("Remarks", "Transaction: " + command);
            json.put("Initiator", chowderCredentials.getInitiatorName());
            json.put("SecurityCredential", chowderCredentials.getSecurityCredential());
            json.put("QueueTimeOutURL", "http://test.com/mpesa");
            json.put("ResultURL", "http://test.com/mpesa");
            json.put("AccountReference", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String conversationId = response.getString("ConversationID");
                    String originatorConversationID = response.getString("OriginatorConversationID");
                    String responseCode = response.getString("ResponseCode");
                    String responseDescription = response.getString("ResponseDescription");

                    showMessage(activity, responseDescription);

                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(activity, "Something went wrong: " + error.getMessage());
                error.printStackTrace();
                progress.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public void c2bPaymentRequest(String command, int amount) {
        String URL = BASE_URL_SANDBOX + API_TYPE_B2C + API_VERSION + END_POINT_PAYMENT_REQUEST;

        final ProgressDialog progress = ProgressDialog.show(activity, "Please wait", "Making payment request...", true);
        JSONObject json = new JSONObject();
        try {
            json.put("CommandID", command);
            json.put("Amount", amount);
            json.put("PartyA", chowderCredentials.getShortCode1());
            json.put("SenderIdentifier", "");
            json.put("PartyB", chowderCredentials.getMsisdn());
            json.put("RecieverIdentifierType", "");
            json.put("Remarks", "Transaction: " + command);
            json.put("InitiatorName", "http://test.com/mpesa");
            json.put("SecurityCredential", chowderCredentials.getSecurityCredential());
            json.put("QueueTimeOutURL", "http://test.com/mpesa");
            json.put("ResultURL", "http://test.com/mpesa");
            json.put("AccountReference", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String conversationId = response.getString("ConversationID");
                    String originatorConversationID = response.getString("OriginatorConversationID");
                    String responseCode = response.getString("ResponseCode");
                    String responseDescription = response.getString("ResponseDescription");

                    showMessage(activity, responseDescription);

                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showMessage(activity, "Something went wrong: " + error.getMessage());
                error.printStackTrace();
                progress.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}