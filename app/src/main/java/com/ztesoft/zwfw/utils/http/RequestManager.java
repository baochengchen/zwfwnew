package com.ztesoft.zwfw.utils.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ztesoft.zwfw.utils.APPPreferenceManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {
    //超时时间
    private static final int TIMEOUT_COUNT = 30 * 1000;

    private static final int RETRY_TIMES = 1;

    private volatile static RequestManager instance = null;

    private RequestQueue mRequestQueue = null;

    public static boolean DEBUG = true;

    private Context context;

    public interface RequestListener {

        void onRequest(String url, int actionId);

        void onSuccess(String response, String url, int actionId);

        void onError(String errorMsg, String url, int actionId);

    }

    private RequestManager() {

    }

    public void init(Context context) {
        this.context = context;
        this.mRequestQueue = Volley.newRequestQueue(context);
    }

    public static RequestManager getInstance() {
        if (null == instance) {
            synchronized (RequestManager.class) {
                if (null == instance) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public LoadController getHeader(final String url, final RequestListener requestListener, int actionId, HashMap<String, String> headers) {
        headers.put("Content-Type", "application/json");
        headers.put("Cookie",APPPreferenceManager.getInstance().getString(context,"session"));
        return this.request(Method.GET, url, null, requestListener, actionId, headers);
    }

    public LoadController post(final String url, Map<String, String> data, final RequestListener requestListener, int actionId) {
        return this.request(Method.POST, url, data, requestListener, actionId, null);
    }

    public LoadController postHeader(final String url, Map<String, String> data, final RequestListener requestListener, int actionId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return this.request(Method.POST, url, data, requestListener, actionId, headers);
    }


    public LoadController post(final String url, Object object, final RequestListener requestListener, int actionId) {
        return this.request(Method.POST, url, object, requestListener, actionId, null);
    }

    public LoadController postHeader(final String url, Object object, final RequestListener requestListener, int actionId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cookie",APPPreferenceManager.getInstance().getString(context,"session"));
        return this.request(Method.POST, url, object, requestListener, actionId, headers);
    }

    public LoadController patchHeader(final String url, Object object, final RequestListener requestListener, int actionId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Cookie",APPPreferenceManager.getInstance().getString(context,"session"));
        return this.request(Method.PATCH, url, object, requestListener, actionId, headers);
    }

    public LoadController post(final String url, Object object, final RequestListener requestListener, int actionId, HashMap<String, String> headers) {
        return this.request(Method.POST, url, object, requestListener, actionId, headers);
    }

    public LoadController upload(final String url, RequestMap map, final RequestListener requestListener, final int actionId) {
        return this.upload(Method.POST, url, map, new LoadListener() {

            @Override
            public void onSuccess(String data, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onSuccess:" + data);
                    System.out.println("onSuccess:" + url);
                    System.out.println("onSuccess:" + actionId);
                }
                requestListener.onSuccess(data, url, actionId);
            }

            @Override
            public void onStart() {
                requestListener.onRequest(url, actionId);
            }


            @Override
            public void onError(String errorMsg, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onError:" + errorMsg);
                    System.out.println("onError:" + url);
                    System.out.println("onError:" + actionId);
                }
                requestListener.onError(errorMsg, url, actionId);
            }
        }, actionId);
    }

    public LoadController upload(int method, final String url, RequestMap map, final LoadListener requestListener, int actionId) {
        if (requestListener == null)
            throw new NullPointerException();

        final StringLoadController loadController = new StringLoadController(requestListener, actionId);
        Request<?> request = null;
        if (map != null) {
            request = new ByteArrayRequest(method, url, map, loadController, loadController) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    // TODO Auto-generated method stub
                    Map<String, String> headers = Collections.emptyMap();
                    if (null == headers || headers.equals(Collections.emptyMap())) {
                        headers = new HashMap<String, String>();
                    }

                    return headers;
                }
            };
            request.setShouldCache(false);
        }

        RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_COUNT, RETRY_TIMES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        loadController.bindRequest(request);

        if (this.mRequestQueue == null)
            throw new NullPointerException();
        requestListener.onStart();
        this.mRequestQueue.add(request);

        return loadController;
    }

    public LoadController request(int method, final String url, Object object, final RequestListener requestListener, final int actionId, HashMap<String, String> headers) {
        return this.request(method, url, object, new LoadListener() {

            @Override
            public void onSuccess(String data, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onSuccess:" + data);
                    System.out.println("onSuccess:" + url);
                    System.out.println("onSuccess:" + actionId);
                }
                requestListener.onSuccess(data, url, actionId);
            }

            @Override
            public void onStart() {
                requestListener.onRequest(url, actionId);
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onError:" + errorMsg);
                    System.out.println("onError:" + url);
                    System.out.println("onError:" + actionId);
                }
                requestListener.onError(errorMsg, url, actionId);
            }
        }, actionId, headers);
    }

    public LoadController request(int method, final String url, Map<String, String> data, final RequestListener requestListener, final int actionId, HashMap<String, String> headers) {
        return this.request(method, url, data, new LoadListener() {

            @Override
            public void onSuccess(String data, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onSuccess:" + data);
                    System.out.println("onSuccess:" + url);
                    System.out.println("onSuccess:" + actionId);
                }
                requestListener.onSuccess(data, url, actionId);
            }

            @Override
            public void onStart() {
                requestListener.onRequest(url, actionId);
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                if (DEBUG) {
                    System.out.println("onError:" + errorMsg);
                    System.out.println("onError:" + url);
                    System.out.println("onError:" + actionId);
                }
                requestListener.onError(errorMsg, url, actionId);
            }

        }, actionId, headers);
    }

    public LoadController request(int method, final String url, final Object object, final LoadListener requestListener, int actionId, final HashMap<String, String> headers) {
        if (requestListener == null) {
            throw new NullPointerException("null requestListener");
        }
        final StringLoadController loadController = new StringLoadController(requestListener, actionId);
        Request<?> request = null;

        request = new ByteArrayRequest(method, url, object, loadController, loadController) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers != null) {
                    return headers;
                } else {
                    return new HashMap<String, String>();
                }
            }

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_COUNT, RETRY_TIMES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        loadController.bindRequest(request);

        if (this.mRequestQueue == null) {
            throw new NullPointerException("null RequestQueue");
        }
        requestListener.onStart();
        this.mRequestQueue.add(request);
        return loadController;
    }


    public LoadController request(int method, final String url, final Map<String, String> data, final LoadListener requestListener, int actionId, final HashMap<String, String> headers) {
        if (requestListener == null) {
            throw new NullPointerException("null requestListener");
        }

        final StringLoadController loadController = new StringLoadController(requestListener, actionId);
        StringRequest request = new StringRequest(method, url, loadController, loadController) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return data;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (headers != null) {
                    return headers;
                } else {
                    return new HashMap<String, String>();
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                int statusCode = response.statusCode;
                if (statusCode == 200) {
                    String result = new String(response.data);
                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                } else if (statusCode == 304) {
                    String result = new String(response.data);
                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    Throwable throwable = new Throwable("" + statusCode);
                    VolleyError error = new VolleyError(throwable);
                    return Response.error(error);
                }
            }

        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_COUNT, RETRY_TIMES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        loadController.bindRequest(request);

        if (this.mRequestQueue == null) {
            throw new NullPointerException("null RequestQueue");
        }
        requestListener.onStart();
        this.mRequestQueue.add(request);
        return loadController;
    }

}




















