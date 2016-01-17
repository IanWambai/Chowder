package com.toe.chowder.responses;

import com.alexgilleran.icesoap.annotation.XMLField;
import com.alexgilleran.icesoap.annotation.XMLObject;

//<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns1="tns:ns">
//  <SOAP-ENV:Body>
//     <ns1:processCheckOutResponse>
//        <RETURN_CODE>00</RETURN_CODE>
//        <DESCRIPTION>Success</DESCRIPTION>
//        <TRX_ID>cce3d32e0159c1e62a9ec45b67676200</TRX_ID>
//        <ENC_PARAMS/>
//        <CUST_MSG>To complete this transaction, enter your Bonga PIN on your handset. if you don't have one dial *126*5# for instructions</CUST_MSG>
//     </ns1:processCheckOutResponse>
//  </SOAP-ENV:Body>
//</SOAP-ENV:Envelope>

@XMLObject("/Envelope/Body/processCheckOutResponse")
public class ProcessCheckoutResponse {

    @XMLField("RETURN_CODE")
    private String returnCode;

    @XMLField("DESCRIPTION")
    private String description;

    @XMLField("TRX_ID")
    private String transactionId;

    @XMLField("ENC_PARAMS")
    private String encParams;

    @XMLField("CUST_MSG")
    private String customMessage;

    public String getReturnCode() {
        return returnCode;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getEncParams() {
        return encParams;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}