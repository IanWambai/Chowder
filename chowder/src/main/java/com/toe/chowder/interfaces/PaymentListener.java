package com.toe.chowder.interfaces;

/**
 * Created by Wednesday on 03/31/2016.
 */
public interface PaymentListener {
    void onPaymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId);

    void onPaymentSuccess(String merchantId, String phoneNumber, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId);

    void onPaymentFailure(String merchantId, String phoneNumber, String amount, String transactionStatus, String processDescription);
}
