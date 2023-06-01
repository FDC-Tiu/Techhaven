package com.example.techhaven;

import android.app.NotificationManager;
import android.app.Application;
import android.nfc.Tag;
import android.text.TextUtils;
import android.app.NotificationChannel;
import android.os.Build;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private RequestQueue mRequestQueue;
    private static VolleySingleton mInstance;
    public static final String TAG = VolleySingleton.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized VolleySingleton getmInstance() {
        return mInstance;
    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag((TextUtils.isEmpty(tag) ? TAG :tag));
        getmRequestQueue().add(request);

        //request.setTag((TextUtils.isEmpty(tag) ? TAG :tag));
        //getmRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        getmRequestQueue().add(request);

        //request.setTag(TAG);
        //getmRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}