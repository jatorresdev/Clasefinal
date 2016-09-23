package com.example.aprendiz.clasefinal.clients;



import android.content.Context;
import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
/**
 * Created by APRENDIZ on 21/09/2016.
 */
public class PublishRestClient {


    private static final String BASE_URL = "http://phplaravel-26935-58004-154595.cloudwaysapps.com/api/publication";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Fragment context, String url, Header[] headers, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {
        client.get(context.getActivity(), getAbsoluteUrl(url), headers, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
