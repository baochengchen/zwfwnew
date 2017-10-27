package com.ztesoft.zwfw.utils.http;

import com.android.volley.Request;

public interface LoadController {
	void cancel();
}

class AbsLoadControler implements LoadController {
	
	protected Request<?> mRequest;

	public void bindRequest(Request<?> request) {
		this.mRequest = request;
	}

	@Override
	public void cancel() {
		if (this.mRequest != null) {
			this.mRequest.cancel();
		}
	}

	protected String getOriginUrl() {
		return this.mRequest.getUrl();
	}
}
