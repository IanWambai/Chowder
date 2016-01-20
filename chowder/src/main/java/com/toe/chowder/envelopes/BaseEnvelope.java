package com.toe.chowder.envelopes;

import com.alexgilleran.icesoap.envelope.impl.BaseSOAP11Envelope;

/**
 * Created by Wednesday on 1/15/2016.
 */
public abstract class BaseEnvelope extends BaseSOAP11Envelope {

    private final static String TNS_NAMESPACE = "tns:ns";
    private final static String NO_NAMESPACE = "";

    public BaseEnvelope() {
        declarePrefix("tns", TNS_NAMESPACE);
    }

    protected String getTnsNamespace() {
        return TNS_NAMESPACE;
    }

    public static String getNoNamespace() {
        return NO_NAMESPACE;
    }
}
