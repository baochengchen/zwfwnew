package com.ztesoft.zwfw.utils.http;

public interface LoadListener {
	
	void onStart();

	void onSuccess(String data, String url, int actionId);

	void onError(String errorMsg, String url, int actionId);
}
