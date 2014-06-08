package com.lm.lib.http_net_lib.callback;

import com.lm.lib.http_net_lib.utilities.IOUtilities;
import com.lm.lib.http_net_lib.utilities.TextUtil;

public abstract class StringCallback extends AbstractCallback<Object> {

	@Override
	protected Object bindData(String content) {
		if (TextUtil.isValidate(path)) {
			return IOUtilities.readFromFile(path);
		}
		return content;
	}

}
