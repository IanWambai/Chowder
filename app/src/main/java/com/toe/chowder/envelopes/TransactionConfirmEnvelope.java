package com.toe.chowder.envelopes;

import com.alexgilleran.icesoap.xml.XMLParentNode;

/**
 * Created by Wednesday on 1/15/2016.
 */
public class TransactionConfirmEnvelope extends BaseEnvelope {

    public TransactionConfirmEnvelope(String merchantId, String password, String timestamp, String transactionId, String merchantTransactionId) {
        //Header
        XMLParentNode checkoutHeader = getHeader().addNode(getTnsNamespace(), "CheckOutHeader");
        checkoutHeader.addTextNode(getNoNamespace(), "MERCHANT_ID", merchantId);
        checkoutHeader.addTextNode(getNoNamespace(), "PASSWORD", password);
        checkoutHeader.addTextNode(getNoNamespace(), "TIMESTAMP", timestamp);

        //Body
        XMLParentNode processCheckOutRequest = getBody().addNode(
                getTnsNamespace(), "transactionConfirmRequest");
        //Optional
        processCheckOutRequest.addTextNode(getNoNamespace(), "TRX_ID", transactionId);
        processCheckOutRequest.addTextNode(getNoNamespace(), "MERCHANT_TRANSACTION_ID", merchantTransactionId);
    }
}
