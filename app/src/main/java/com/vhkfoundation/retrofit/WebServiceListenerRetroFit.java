package com.vhkfoundation.retrofit;

import org.json.JSONException;

public interface WebServiceListenerRetroFit {
    void onWebServiceRetroActionComplete(String result, String url) throws JSONException;
    void onWebServiceRetroError(String result, String url);
}
