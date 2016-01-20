package com.toe.chowder.responses;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

//<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns1="tns:ns">
//        <SOAP-ENV:Body>
//        <ns1:transactionStatusResponse>
//        <MSISDN>254720471865</MSISDN>
//        <AMOUNT>54000</AMOUNT>
//        <M-PESA_TRX_DATE>2014-12-01 16:59:07</M-PESA_TRX_DATE>
//        <M-PESA_TRX_ID>N/A</M-PESA_TRX_ID>
//        <TRX_STATUS>Failed</TRX_STATUS>
//        <RETURN_CODE>01</RETURN_CODE>
//        <DESCRIPTION>InsufficientFunds</DESCRIPTION>
//        <MERCHANT_TRANSACTION_ID/>
//        <ENC_PARAMS/>
//        <TRX_ID>ddd396509b168297141a747cd2dc1748</TRX_ID>
//        </ns1:transactionStatusResponse>
//        </SOAP-ENV:Body>
//</SOAP-ENV:Envelope>


@XMLObject("/Envelope/Body/transactionStatusResponse")
public class TransactionStatusQueryResponse {

    @XMLField("MSISDN")
    private String msisdn;

    @XMLField("AMOUNT")
    private String amount;

    @XMLField("M-PESA_TRX_DATE")
    private String mpesaTransactionDate;

    @XMLField("M-PESA_TRX_ID")
    private String mpesaTransactionId;

    @XMLField("TRX_STATUS")
    private String transactionStatus;

    @XMLField("RETURN_CODE")
    private String returnCode;

    @XMLField("DESCRIPTION")
    private String description;

    @XMLField("MERCHANT_TRANSACTION_ID")
    private String merchantTransactionId;

    @XMLField("ENC_PARAMS")
    private String encParams;

    @XMLField("TRX_ID")
    private String transactionId;

    public String getMsisdn() {
        return msisdn;
    }

    public String getAmount() {
        return amount;
    }

    public String getMpesaTransactionDate() {
        return mpesaTransactionDate;
    }

    public String getMpesaTransactionId() {
        return mpesaTransactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getDescription() {
        return description;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public String getEncParams() {
        return encParams;
    }

    public String getTransactionId() {
        return transactionId;
    }
}