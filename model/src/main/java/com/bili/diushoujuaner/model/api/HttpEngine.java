package com.bili.diushoujuaner.model.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by BiLi on 2016/3/11.
 */
public class HttpEngine {

    private static HttpEngine instance;
    private static RequestQueue requestQueue;
    private static Context ctx;

    public static void initialize(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
        instance = new HttpEngine();
    }

    public static synchronized HttpEngine getInstance() {
        if (instance == null) {
            throw new NullPointerException("HttpEngine was not initialized!");
        }
        return instance;
    }

    private static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            int diskCacheSize = 50 * 1024 * 1024; // 50MB
            requestQueue = Volley.newRequestQueue(ctx, diskCacheSize);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        this.requestQueue.add(req);
    }

    public void clearDiskCache() {
        this.requestQueue.getCache().clear();
    }

}
