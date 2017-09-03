package com.toe.chowder.classes;

import android.content.Context;

import com.alexgilleran.icesoap.request.impl.ApacheSOAPRequester;
import com.toe.chowder.utils.ChowderUtils;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;

/**
 * Created by ian on 04/09/2017.
 */

public class ChowderSOAPRequester extends ApacheSOAPRequester {

    private final Context context;

    public ChowderSOAPRequester(Context context) {
        this.context = context;
    }

    @Override
    protected SchemeRegistry getSchemeRegistry() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme(HTTPS_NAME, ChowderUtils.hostnameVerification(context), DEFAULT_HTTPS_PORT));
        return schemeRegistry;
    }
}
