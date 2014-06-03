package com.lm.lib.http_net_lib.callback;

import com.lm.lib.http_net_lib.utilities.IOUtilities;
import com.lm.lib.http_net_lib.utilities.JsonParser;
import com.lm.lib.http_net_lib.utilities.TextUtil;

public abstract class JsonCallback<T> extends AbstractCallback<T> {

	@Override
	protected T bindData(String content) {
		if (TextUtil.isValidate(path)) {
			content = IOUtilities.readFromFile(path);
		}
		if (returnClass != null) {
			return JsonParser.deserializeByJson(content, returnClass);
		} else if (returnType != null) {
			return JsonParser.deserializeByJson(content, returnType);
		}
		return null;
	}

}
