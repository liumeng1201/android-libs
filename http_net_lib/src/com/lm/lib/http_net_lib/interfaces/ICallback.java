package com.lm.lib.http_net_lib.interfaces;

import org.apache.http.HttpResponse;

import com.lm.lib.http_net_lib.AppException;

public interface ICallback<T> {
	void onFailure(Exception result);
	void onSuccess(T result);
	T handle(HttpResponse response, IProgressListener mProgressListener) throws AppException;
	void checkIfCancelled() throws AppException;
	void cancel();
}
