package com.toe.chowder.envelopes;

import com.alexgilleran.icesoap.xml.XMLParentNode;

/**
 * Created by Wednesday on 1/15/2016.
 */
public class ProcessCheckoutEnvelope extends BaseEnvelope {

    public ProcessCheckoutEnvelope(String merchantId, String password, String timestamp, String merchantTransactionId, String referenceId, String amount, String msisdn, String encParams, String callBackUrl, String callBackMethod) {
        //Header
        XMLParentNode checkoutHeader = getHeader().addNode(getTnsNamespace(), "CheckOutHeader");
        checkoutHeader.addTextNode(getNoNamespace(), "MERCHANT_ID", merchantId);
        checkoutHeader.addTextNode(getNoNamespace(), "PASSWORD", password);
        checkoutHeader.addTextNode(getNoNamespace(), "TIMESTAMP", timestamp);

        //Body
        XMLParentNode processCheckOutRequest = getBody().addNode(
                getTnsNamespace(), "processCheckOutRequest");
        processCheckOutRequest.addTextNode(getNoNamespace(), "MERCHANT_TRANSACTION_ID", merchantTransactionId);
        processCheckOutRequest.addTextNode(getNoNamespace(), "REFERENCE_ID", referenceId);
        processCheckOutRequest.addTextNode(getNoNamespace(), "AMOUNT", amount);
        processCheckOutRequest.addTextNode(getNoNamespace(), "MSISDN", msisdn);
        //Optional
        processCheckOutRequest.addTextNode(getNoNamespace(), "ENC_PARAMS", encParams);
        processCheckOutRequest.addTextNode(getNoNamespace(), "CALL_BACK_URL", callBackUrl);
        processCheckOutRequest.addTextNode(getNoNamespace(), "CALL_BACK_METHOD", callBackMethod);
        processCheckOutRequest.addTextNode(getNoNamespace(), "TIMESTAMP", timestamp);
    }
}
