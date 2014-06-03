package com.lm.lib.http_net_lib.interfaces;

public interface IProgressListener {
	void onProgressUpdate(int curPos, int contentLength);
}
