package com.example.android.bakemaster.rest;

import okhttp3.OkHttpClient;

/**
 * OkHttpProvider for testing purposes of MainActivity.
 */
public abstract class OkHttpProvider {
    private static OkHttpClient instance = null;

    public static OkHttpClient getOkHttpInstance() {
        if (instance == null) {
            instance = new OkHttpClient();
        }
        return instance;
    }
}