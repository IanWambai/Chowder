package com.toe.chowder.responses;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

//<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns1="tns:ns">
//        <SOAP-ENV:Body>
//        <ns1:transactionStatusResponse>
//        <MSISDN>254700177140</MSISDN>
//        <AMOUNT>10</AMOUNT>
//        <MPESA_TRX_DATE>2016-03-31 06:58:24</MPESA_TRX_DATE>
//        <MPESA_TRX_ID>KCV3E6591D</MPESA_TRX_ID>
//        <TRX_STATUS>Success</TRX_STATUS>
//        <RETURN_CODE>00</RETURN_CODE>
//        <DESCRIPTION>The service request is processed successfully.</DESCRIPTION>
//        <MERCHANT_TRANSACTION_ID/>
//        <ENC_PARAMS/>
//        <TRX_ID>ee35a7f5829bf4e31da2e0f09c442720</TRX_ID>
//        </ns1:transactionStatusResponse>
//        </SOAP-ENV:Body>
//</SOAP-ENV:Envelope>


@XMLObject("/Envelope/Body/transactionStatusResponse")
public class TransactionStatusQueryResponse {

    @XMLField("MSISDN")
    private String msisdn;

    @XMLField("AMOUNT")
    private String amount;

    @XMLField("MPESA_TRX_DATE")
    private String mpesaTransactionDate;

    @XMLField("MPESA_TRX_ID")
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