package com.lm.lib.http_net_lib;

public class AppException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum EnumException {
		ParseException, CancelException, IOException, NormallException, ClientProtocolException
	}

	private EnumException mExceptionType;
	private String detailMessage;

	public AppException(EnumException type, String detailMessage) {
		super(detailMessage);
		this.mExceptionType = type;
		this.detailMessage = detailMessage;
	}
}
