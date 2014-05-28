package com.lm.lib.http_net_lib;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientUtil {

	public static HttpResponse execute(Request request) throws Exception {
		switch (request.method) {
		case GET:
			return get(request);
		case POST:
			return post(request);
		default:
			throw new IllegalStateException("the method "
					+ request.method.name() + " doesn't support");
		}
	}

	public static HttpResponse get(Request request) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(request.url);
		addHeader(get, request.headers);
		HttpResponse response = client.execute(get);
		return response;
	}

	public static HttpResponse post(Request request) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(request.url);
		addHeader(post, request.headers);
		if (request.entity == null) {
			throw new IllegalStateException("you forget to set post content to the HttpPost");
		} else {
			post.setEntity(request.entity);
		}
		HttpResponse response = client.execute(post);
		return response;
	}

	public static void addHeader(HttpUriRequest request,
			Map<String, String> headers) {
		if (headers != null && headers.size() > 0) {
			for (Entry<String, String> entry : headers.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
}
