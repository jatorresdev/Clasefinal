package com.example.aprendiz.salesapp.application;

import android.app.Application;
import android.text.TextUtils;



/**
 * Created by APRENDIZ on 20/09/2016.
 */
public class ConfigureApp extends Application {
/*
public  static  final  String TAG = ConfigureApp.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private  static  ConfigureApp mInstance;


    @Override
    public  void onCreate(){
        super.onCreate();
        mInstance=this;

    }

    public static synchronized ConfigureApp getmInstance(){
        return  mInstance;

    }

    public RequestQueue getmRequestQueue(){

        if(mRequestQueue ==null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        }
        return mRequestQueue;

    }

    public  <T> void  addToRequestQueue(Request<T> req, String tag ){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(req);



    }

    public void cancelPendingRequests(Object tag)
    {
        if(mRequestQueue !=null)
        {
            mRequestQueue.cancelAll(tag);

        }

    }
*/
}
