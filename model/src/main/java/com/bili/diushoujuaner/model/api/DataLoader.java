package com.bili.diushoujuaner.model.api;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bili.diushoujuaner.model.api.callback.ApiCallbackListener;
import com.bili.diushoujuaner.model.preference.AccessTokenPreference;

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

    public void processJsonObjectRequest(String url, final Map<String, String> map, final ApiCallbackListener apiCallbackListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                headers.put("AccessToken", AccessTokenPreference.getInstance().getAccessToken().getAccessToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        HttpEngine.getInstance().addToRequestQueue(stringRequest);
    }

}
