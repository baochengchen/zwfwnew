package com.ztesoft.zwfw.utils.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.moudle.ZWFWApp;
import com.ztesoft.zwfw.utils.APPPreferenceManager;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class ByteArrayRequest extends Request<byte[]> {

    private final Listener<String> mListener;

    private Object mPostBody = null;

    private HttpEntity httpEntity = null;

    public ByteArrayRequest(int method, String url, Object postBody, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mPostBody = postBody;
        this.mListener = listener;

        if (this.mPostBody != null && this.mPostBody instanceof RequestMap) {// contains file
            this.httpEntity = ((RequestMap) this.mPostBody).getEntity();
        }
    }

    /**
     * mPostBody is null or Map<String, String>, then execute this method
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getParams() throws AuthFailureError {
        if (this.httpEntity == null && this.mPostBody != null && this.mPostBody instanceof Map<?, ?>) {
            return ((Map<String, String>) this.mPostBody);//common Map<String, String>
        }
        return null;//process as json, xml or MultipartRequestParams
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        //headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        if (httpEntity != null) {
            return httpEntity.getContentType().getValue();
        }
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String) {//process as json or xml
            String postString = (String) mPostBody;
            if (postString.length() != 0) {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        if (this.httpEntity != null) {//process as MultipartRequestParams
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();// mPostBody is null or Map<String, String>
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //获取cookie头部信息
        Map<String, String> responseHeaders = response.headers;
        String rawCookies = responseHeaders.get("Set-Cookie");
        //分隔获取sessionid
        if (null != rawCookies) {
            String[] splitCookie = rawCookies.split(";");
            if (!APPPreferenceManager.getInstance().getBool(ZWFWApp.appContext, Config.IS_LOGIN)){
                APPPreferenceManager.getInstance().saveObject(ZWFWApp.appContext, "session", splitCookie[0]);
            }
        }

        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(new String(response));
    }

}