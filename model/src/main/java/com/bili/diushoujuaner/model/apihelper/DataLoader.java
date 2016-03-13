package com.bili.diushoujuaner.model.apihelper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bili.diushoujuaner.model.apihelper.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/11.
 */
public class DataLoader {

    private Context context;
    public static final String TAG = "DataLoader";

    public DataLoader(Context context) {
        this.context = context;
    }

    public void processStringRequest(int method, String url, final Map<String, String> map, final ApiCallbackListener apiCallbackListener) {
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.json(response);
                apiCallbackListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                switch (error.networkResponse != null ? error.networkResponse.statusCode : 100) {
                    case 401:
                    case 403:
                        HttpEngine.getInstance().clearDiskCache();
                        apiCallbackListener.onFailure(403);
                        break;
                    case 500:
                        apiCallbackListener.onFailure(500);
                        break;
                    case 100:
                        apiCallbackListener.onFailure(100);
                        break;
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Device-Type", "Client/Android");
                headers.put("AccessToken", CustomSessionPreference.getInstance().getCustomSession().getAccessToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        stringRequest.setShouldCache(false);
        HttpEngine.getInstance().addToRequestQueue(stringRequest);
    }

}
