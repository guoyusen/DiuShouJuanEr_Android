package com.bili.diushoujuaner.model.apihelper;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bili.diushoujuaner.model.apihelper.callback.ApiFileCallbackListener;
import com.bili.diushoujuaner.model.apihelper.callback.ApiStringCallbackListener;
import com.bili.diushoujuaner.utils.okhttp.okhttpserver.listener.UploadListener;
import com.bili.diushoujuaner.utils.okhttp.okhttpserver.upload.UploadInfo;
import com.bili.diushoujuaner.utils.okhttp.okhttpserver.upload.UploadManager;
import com.bili.diushoujuaner.utils.okhttp.okhttputils.OkHttpUtils;
import com.bili.diushoujuaner.utils.okhttp.okhttputils.model.HttpHeaders;
import com.bili.diushoujuaner.model.preferhelper.CustomSessionPreference;
import com.bili.diushoujuaner.utils.ConstantUtil;
import com.bili.diushoujuaner.utils.okhttp.okhttputils.model.HttpParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BiLi on 2016/3/11.
 */
public class DataLoader {

    public static final String TAG = "DataLoader";

    public DataLoader() {
    }

    public void processStringRequest(int method, String url, final Map<String, String> params, final ApiStringCallbackListener apiStringCallbackListener) {
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Logger.json(response);
                apiStringCallbackListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                switch (error.networkResponse != null ? error.networkResponse.statusCode : 100) {
                    case 401:
                    case 403:
                        HttpEngine.getInstance().clearDiskCache();
                        apiStringCallbackListener.onFailure(403);
                        break;
                    case 500:
                        apiStringCallbackListener.onFailure(500);
                        break;
                    case 100:
                        apiStringCallbackListener.onFailure(100);
                        break;
                    case 503:
                        apiStringCallbackListener.onFailure(503);
                        break;
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Charset", "UTF-8");
                headers.put("Device-Type", "Client/Android");
                headers.put("AccessToken", CustomSessionPreference.getInstance().getCustomSession().getAccessToken());
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setShouldCache(false);
        HttpEngine.getInstance().addToRequestQueue(stringRequest);
    }

    public void processFileUpload(String url, Map<String, String> params, String key, String path, final ApiFileCallbackListener apiFileCallbackListener){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("Device-Type", "Client/Android");
        httpHeaders.put("AccessToken", CustomSessionPreference.getInstance().getCustomSession().getAccessToken());
        if(params != null){
            HttpParams httpParams = new HttpParams();
            Object[] keyArray = params.keySet().toArray();
            for(int i = 0, len = params.size(); i < len; i++){
                httpParams.put(keyArray[i].toString(),params.get(keyArray[i]));
            }
            OkHttpUtils.getInstance().addCommonParams(httpParams);
        }
        OkHttpUtils.getInstance().addCommonHeaders(httpHeaders);
        UploadManager.getInstance().addTask(url, new File(path), key, new UploadListener<String>() {
            // 当前回调为主线程
            @Override
            public void onError(UploadInfo uploadInfo, String errorMsg, Exception e) {
                apiFileCallbackListener.onFailure(ConstantUtil.WARNING_FILE);
            }

            @Override
            public void onProgress(UploadInfo uploadInfo) {
                apiFileCallbackListener.onProgress(uploadInfo.getProgress());
            }

            @Override
            public void onFinish(String s) {
                apiFileCallbackListener.onSuccess(s);
            }

            @Override
            public String parseNetworkResponse(okhttp3.Response response) throws Exception {
                return response.body().string();
            }
        });
    }

}
