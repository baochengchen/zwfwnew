package com.ztesoft.zwfw.utils.http;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class StringLoadController extends AbsLoadControler implements Listener<String>, ErrorListener
{
    private LoadListener mOnLoadListener;
    
    private int mAction = 0;
    
    public StringLoadController(LoadListener requestListener, int actionId)
    {
        this.mOnLoadListener = requestListener;
        this.mAction = actionId;
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        // TODO Auto-generated method stub
        String errorMsg = null;
        if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        } else {
            try {
                //errorMsg = "Server Response Error (" + error.networkResponse.statusCode + ")";
                errorMsg = new String(error.networkResponse.data);
            } catch (Exception e) {
                errorMsg = "Server Response Error";
            }
        }
        this.mOnLoadListener.onError(errorMsg, getOriginUrl(), this.mAction);
    }

    @Override
    public void onResponse(String response)
    {
        // TODO Auto-generated method stub
        this.mOnLoadListener.onSuccess(response, getOriginUrl(), this.mAction);
    }
    
}
