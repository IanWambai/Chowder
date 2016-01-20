package com.toe.chowder.responses;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

//<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns1="tns:ns">
//        <SOAP-ENV:Body>
//        <ns1:transactionConfirmResponse>
//        <RETURN_CODE>00</RETURN_CODE>
//        <DESCRIPTION>Success</DESCRIPTION>
//        <MERCHANT_TRANSACTION_ID/>
//        <TRX_ID>5f6af12be0800c4ffabb4cf2608f0808</TRX_ID>
//        </ns1:transactionConfirmResponse>
//        </SOAP-ENV:Body>
//</SOAP-ENV:Envelope>

@XMLObject("/Envelope/Body/transactionConfirmResponse")
public class TransactionConfirmResponse {

    @XMLField("RETURN_CODE")
    private String returnCode;

    @XMLField("DESCRIPTION")
    private String description;

    @XMLField("MERCHANT_TRANSACTION_ID")
    private String merchantTransactionId;

    @XMLField("TRX_ID")
    private String transactionId;

    public String getReturnCode() {
        return returnCode;
    }

    public String getDescription() {
        return description;
    }

    public String getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}