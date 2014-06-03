package com.lm.lib.http_net_lib;

import org.apache.http.HttpResponse;

import com.lm.lib.http_net_lib.interfaces.IProgressListener;

import android.os.AsyncTask;

public class RequestTask extends AsyncTask<Object, Integer, Object> {

	private Request request;

	public RequestTask(Request request) {
		this.request = request;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Object doInBackground(Object... params) {
		try {
			HttpResponse response = HttpClientUtil.execute(request);
			if (request.mProgressListener != null) {
				return request.callback.handle(response, new IProgressListener() {
					@Override
					public void onProgressUpdate(int curPos, int contentLength) {
						publishProgress(curPos, contentLength);
					}
				});
			} else {
				return request.callback.handle(response, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (request.mProgressListener != null) {
			request.mProgressListener.onProgressUpdate(values[0], values[1]);
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (result instanceof Exception) {
			request.callback.onFailure((Exception)result);
		} else {
			request.callback.onSuccess(result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

}
