package com.toe.chowder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.alexgilleran.icesoap.exception.SOAPException;
import com.alexgilleran.icesoap.observer.SOAP11Observer;
import com.alexgilleran.icesoap.request.Request;
import com.alexgilleran.icesoap.request.RequestFactory;
import com.alexgilleran.icesoap.request.SOAP11Request;
import com.alexgilleran.icesoap.request.impl.RequestFactoryImpl;
import com.alexgilleran.icesoap.soapfault.SOAP11Fault;
import com.toe.chowder.data.Subscription;
import com.toe.chowder.envelopes.ProcessCheckoutEnvelope;
import com.toe.chowder.envelopes.TransactionConfirmEnvelope;
import com.toe.chowder.envelopes.TransactionStatusQueryEnvelope;
import com.toe.chowder.interfaces.PaymentListener;
import com.toe.chowder.responses.ProcessCheckoutResponse;
import com.toe.chowder.responses.TransactionConfirmResponse;
import com.toe.chowder.responses.TransactionStatusQueryResponse;
import com.toe.chowder.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Wednesday on 1/16/2016.
 */
public class Chowder {

    //      The Merchant captures the payment details and prepares call to the SAG’s endpoint
    //      The Merchant invokes SAG’s processCheckOut interface
    //      The SAG validates the request sent and returns a response
    //      Merchant receives the processCheckoutResponse parameters namely
    //      TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and CUST_MSG (Customer message)
    //      The merchant is supposed to display the CUST_MSG to the customer after which the merchant should invoke SAG’s confirmPaymentRequest interface to confirm the transaction
    //      The system will push a USSD menu to the customer and prompt the customer to enter their BONGA PIN and any other validation information.
    //      The transaction is processed on M-PESA and a callback is executed after completion of the transaction

    private String SUCCESS_CODE = "00";
    private RequestFactory requestFactory = new RequestFactoryImpl();

    //Need the url and SOAP action
    private String url = "https://safaricom.co.ke/mpesa_online/lnmo_checkout_server.php?wsdl";
    private String soapAction = "https://safaricom.co.ke/mpesa_online/lnmo_checkout_server.php?wsdl";

    //M-Pesa checkout parameters
    private String encParams = "";
    private String callBackUrl = "http://ian.co.ke/hosting/chowder/callback.php";
    private String callBackMethod = "POST";

    //Payment parameters
    private String merchantId;
    private String passkey;
    private String merchantTransactionId;
    private String timestamp;
    private String password;

    //Listeners
    PaymentListener paymentCompleteListener;

    //Subscriptions
    public final static int SUBSCRIBE_DAILY = 1;
    public final static int SUBSCRIBE_WEEKLY = 2;
    public final static int SUBSCRIBE_MONTHLY = 3;
    public final static int SUBSCRIBE_YEARLY = 4;

    private int SELECTED_SUBSCRIPTION = 0;

    //Others
    private Activity activity;
    private ProgressDialog progress;

    public Chowder(Activity activity, String merchantId, String passkey, PaymentListener paymentCompleteListener) {
        this.paymentCompleteListener = paymentCompleteListener;
        this.activity = activity;
        this.merchantId = merchantId;
        this.passkey = passkey;
    }

    public Chowder(Activity activity, String merchantId, String passkey, String callBackUrl, PaymentListener paymentCompleteListener) {
        this.paymentCompleteListener = paymentCompleteListener;
        this.activity = activity;
        this.merchantId = merchantId;
        this.passkey = passkey;
        this.callBackUrl = callBackUrl;
    }

    public void processPayment(String amount, String phoneNumber, String productId) {
        progress = ProgressDialog.show(activity, "Please wait", "Connecting to Safaricom...", true);

        String referenceId = productId;
        timestamp = Utils.generateTimestamp();
        merchantTransactionId = Utils.generateRandomId();
        password = Utils.generatePassword(merchantId + passkey + timestamp).replaceAll("\\s+", "");

//      The Merchant captures the payment details and prepares call to the SAG’s endpoint.
//      The Merchant invokes SAG’s processCheckOut interface.

        Utils.trustEveryone();
        SOAP11Request<ProcessCheckoutResponse> processCheckoutRequest = requestFactory.buildRequest(url, new ProcessCheckoutEnvelope(merchantId, password, timestamp, merchantTransactionId, referenceId, amount, phoneNumber, encParams, callBackUrl, callBackMethod), soapAction, ProcessCheckoutResponse.class);
        processCheckoutRequest.execute(processCheckoutObserver);
    }

    public void subscribeForProduct(String productId, int subscriptionPlan) {
        Calendar calendar = Calendar.getInstance();
        switch (subscriptionPlan) {
            case SUBSCRIBE_DAILY:
                calendar.add(Calendar.DATE, 1);
                break;
            case SUBSCRIBE_WEEKLY:
                calendar.add(Calendar.DATE, 7);
                break;
            case SUBSCRIBE_MONTHLY:
                calendar.add(Calendar.DATE, 30);
                break;
            case SUBSCRIBE_YEARLY:
                calendar.add(Calendar.DATE, 365);
                break;
        }

        saveSubscription(productId, calendar.getTimeInMillis());
    }

    private void saveSubscription(String productId, long subscriptionPeriod) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);

        try {
            JSONArray subscriptions = new JSONArray();

            String subscriptionString = sharedPreferences.getString("subscriptions", null);
            if (subscriptionString != null) {
                subscriptions = new JSONArray(subscriptionString);
            }

            JSONObject subscription = new JSONObject();
            subscription.put("productId", productId);
            subscription.put("period", subscriptionPeriod);

            subscriptions.put(subscription);

            sharedPreferences.edit().putString("subscriptions", subscriptions.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SOAP11Observer<ProcessCheckoutResponse> processCheckoutObserver = new SOAP11Observer<ProcessCheckoutResponse>() {

        @Override
        public void onCompletion(Request<ProcessCheckoutResponse, SOAP11Fault> request) {
            if (request.getResult() != null) {
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String transactionId = request.getResult().getTransactionId();
                String encParams = request.getResult().getEncParams();
                String customMessage = request.getResult().getCustomMessage();

//                The SAG validates the request sent and returns a response.
//                Merchant receives the processCheckoutResponse parameters namely
//                TRX_ID, ENC_PARAMS, RETURN_CODE, DESCRIPTION and
//                CUST_MSG (Customer message).

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
//                   The merchant is supposed to display the CUST_MSG to the customer after which
//                   the merchant should invoke SAG’s confirmPaymentRequest
//                   interface to confirm the transaction

                    showCustomMessage(activity, customMessage, transactionId);
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Process checkout failed: " + returnCode);
                    Toast.makeText(activity, "Something went wrong. Checkout failed: " + returnCode, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("M-PESA REQUEST", "Result is null");
                Toast.makeText(activity, "Something went wrong. No response from Safaricom. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Request<ProcessCheckoutResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void showCustomMessage(final Context context, String customMessage, final String transactionId) {
        new AlertDialog.Builder(context)
                .setTitle("Payment Ready")
                .setMessage(customMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progress = ProgressDialog.show(context, "Please wait",
                                "Processing your payment...", true);
                        SOAP11Request<TransactionConfirmResponse> transactionConfirmRequest = requestFactory.buildRequest(url, new TransactionConfirmEnvelope(merchantId, password, timestamp, transactionId, merchantTransactionId), soapAction, TransactionConfirmResponse.class);
                        transactionConfirmRequest.execute(transactionConfirmObserver);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private SOAP11Observer<TransactionConfirmResponse> transactionConfirmObserver = new SOAP11Observer<TransactionConfirmResponse>() {

        @Override
        public void onCompletion(Request<TransactionConfirmResponse, SOAP11Fault> request) {
//            The system will push a USSD menu to the customer and prompt the customer to enter
//            their BONGA PIN and any other validation information.
//            The transaction is processed on M-PESA and a callback is executed after completion of the transaction.

            if (request.getResult() != null) {
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String merchantTransactionId = request.getResult().getMerchantTransactionId();
                String transactionId = request.getResult().getTransactionId();

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
                    paymentReady(returnCode, processDescription, merchantTransactionId, transactionId);
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Transaction confirmation failed: " + returnCode);
                    Toast.makeText(activity, "Something went wrong. Transaction confirmation failed: " + returnCode, Toast.LENGTH_SHORT).show();
                }
            } else {
                progress.dismiss();
                Log.d("M-PESA REQUEST", "Result is null");
                Toast.makeText(activity, "Something went wrong. No response from Safaricom. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Request<TransactionConfirmResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void checkTransactionStatus(String merchantId, String transactionId) {
        progress = ProgressDialog.show(activity, "Please wait",
                "Checking the transaction status...", true);

        timestamp = Utils.generateTimestamp();
        merchantTransactionId = Utils.generateRandomId();
        password = Utils.generatePassword(merchantId + passkey + timestamp).replaceAll("\\s+", "");

        Utils.trustEveryone();
        SOAP11Request<TransactionStatusQueryResponse> transactionStatusQueryRequest = requestFactory.buildRequest(url, new TransactionStatusQueryEnvelope(merchantId, password, timestamp, transactionId, merchantTransactionId), soapAction, TransactionStatusQueryResponse.class);
        transactionStatusQueryRequest.execute(transactionStatusQueryObserver);
    }

    private SOAP11Observer<TransactionStatusQueryResponse> transactionStatusQueryObserver = new SOAP11Observer<TransactionStatusQueryResponse>() {

        @Override
        public void onCompletion(Request<TransactionStatusQueryResponse, SOAP11Fault> request) {
            if (request.getResult() != null) {
                String msisdn = request.getResult().getMsisdn();
                String amount = request.getResult().getAmount();
                String mpesaTransactionDate = request.getResult().getMpesaTransactionDate();
                String mpesaTransactionId = request.getResult().getMpesaTransactionId();
                String transactionStatus = request.getResult().getTransactionStatus();
                String returnCode = request.getResult().getReturnCode();
                String processDescription = request.getResult().getDescription();
                String merchantTransactionId = request.getResult().getMerchantTransactionId();
                String encParams = request.getResult().getEncParams();
                String transactionId = request.getResult().getTransactionId();

                if (returnCode.equals(SUCCESS_CODE)) {
                    progress.dismiss();
                    if (mpesaTransactionId == null || mpesaTransactionId.equals("N/A") || mpesaTransactionDate == null) {
                        paymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
                    } else {
                        paymentSuccess(merchantId, msisdn, amount, mpesaTransactionDate, mpesaTransactionId, transactionStatus, returnCode, processDescription, merchantTransactionId, encParams, transactionId);
                    }
                } else {
                    progress.dismiss();
                    Log.d("M-PESA REQUEST", "Transaction confirm failed: " + returnCode);
                    paymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
                }
            } else {
                progress.dismiss();
                Log.d("M-PESA REQUEST", "Result is null");
                Toast.makeText(activity, "Something went wrong. No response from Safaricom. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onException(Request<TransactionStatusQueryResponse, SOAP11Fault> request, SOAPException e) {
            progress.dismiss();
            Log.e("M-PESA REQUEST", "Error: " + e.toString());
            Toast.makeText(activity, "Something went wrong. Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private void paymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId) {
        paymentCompleteListener.onPaymentReady(returnCode, processDescription, merchantTransactionId, transactionId);
    }

    private void paymentSuccess(String merchantId, String msisdn, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        paymentCompleteListener.onPaymentSuccess(merchantId, msisdn, amount, mpesaTransactionDate, mpesaTransactionId, transactionStatus, returnCode, processDescription, merchantTransactionId, encParams, transactionId);
    }

    private void paymentFailure(String merchantId, String msisdn, String amount, String transactionStatus, String processDescription) {
        paymentCompleteListener.onPaymentFailure(merchantId, msisdn, amount, transactionStatus, processDescription);
    }

    public boolean checkSubscription(String queriedProductId) {
        boolean isSubscriptionValid = false;
        JSONObject subscription = searchForSubscription(queriedProductId);

        if (subscription != null) {
            try {
                Date todayDate = new Date();
                Date subscriptionDate = new Date(subscription.getLong("period"));

                if (subscriptionDate.after(todayDate)) {
                    isSubscriptionValid = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return isSubscriptionValid;
    }

    private JSONObject searchForSubscription(String queriedProductId) {
        JSONObject queriedSubscription = null;

        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        try {
            String subscriptionsString = sharedPreferences.getString("subscriptions", null);

            if (subscriptionsString != null) {
                JSONArray subscriptions = new JSONArray(subscriptionsString);

                for (int i = 0; i < subscriptions.length(); i++) {
                    JSONObject subscription = subscriptions.getJSONObject(i);
                    String productId = subscription.getString("productId");

                    if (productId.equals(queriedProductId)) {
                        queriedSubscription = subscription;
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return queriedSubscription;
    }

    public ArrayList<Subscription> checkAllSubscriptions() {
        ArrayList<Subscription> validatedSubscriptions = new ArrayList<>();

        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        try {
            String subscriptionsString = sharedPreferences.getString("subscriptions", null);

            if (subscriptionsString != null) {
                JSONArray subscriptions = new JSONArray(subscriptionsString);

                for (int i = 0; i < subscriptions.length(); i++) {
                    JSONObject subscription = subscriptions.getJSONObject(i);
                    String productId = subscription.getString("productId");
                    long subscriptionPeriod = subscription.getLong("period");
                    boolean isSubscriptionValid = false;

                    Date todayDate = new Date();
                    Date subscriptionDate = new Date(subscription.getLong("period"));

                    if (subscriptionDate.after(todayDate)) {
                        isSubscriptionValid = true;
                    }

                    Subscription validatedSubscription = new Subscription(productId, subscriptionPeriod, isSubscriptionValid);
                    validatedSubscriptions.add(validatedSubscription);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return validatedSubscriptions;
    }
}