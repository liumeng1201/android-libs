package com.lm.lib.http_net_lib.utilities;

public class TextUtil {
	public static boolean isValidate(String content) {
		return (content != null) && (!("".equals(content.trim())));
	}
}
