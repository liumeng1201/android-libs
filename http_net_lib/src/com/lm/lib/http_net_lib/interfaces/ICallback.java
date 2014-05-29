package com.lm.lib.http_net_lib.interfaces;

import org.apache.http.HttpResponse;

public interface ICallback<T> {
	void onFailure(Exception result);
	void onSuccess(T result);
	Object handle(HttpResponse response);
}
