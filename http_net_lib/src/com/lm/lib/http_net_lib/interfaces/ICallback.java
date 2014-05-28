package com.lm.lib.http_net_lib.interfaces;

import org.apache.http.HttpResponse;

public interface ICallback {
	void onFailure(Exception result);
	void onSuccess(Object result);
	Object handle(HttpResponse response);
}
