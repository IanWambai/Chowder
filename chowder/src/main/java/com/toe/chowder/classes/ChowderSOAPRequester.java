package com.toe.chowder.classes;

import com.alexgilleran.icesoap.request.impl.ApacheSOAPRequester;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * Created by ian on 04/09/2017.
 */

public class ChowderSOAPRequester extends ApacheSOAPRequester {

    @Override
    protected SchemeRegistry getSchemeRegistry() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HTTP_NAME, PlainSocketFactory.getSocketFactory(), DEFAULT_HTTP_PORT));
        schemeRegistry.register(new Scheme(HTTPS_NAME, SSLSocketFactory.getSocketFactory(), DEFAULT_HTTPS_PORT));
        return schemeRegistry;
    }
}
