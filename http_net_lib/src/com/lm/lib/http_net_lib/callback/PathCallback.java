package com.lm.lib.http_net_lib.callback;

public abstract class PathCallback extends AbstractCallback<String> {
	@Override
	protected String bindData(String content) {
		return path;
	}
}
