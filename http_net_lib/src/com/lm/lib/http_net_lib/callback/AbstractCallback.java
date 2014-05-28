package com.lm.lib.http_net_lib.callback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.lm.lib.http_net_lib.interfaces.ICallback;
import com.lm.lib.http_net_lib.utilities.TextUtil;

public abstract class AbstractCallback implements ICallback {
	public static final int IO_BUFFER_SIZE = 4 * 1024;
	public String path;

	public Object handle(HttpResponse response) {
		// file, json, xml, image, string
		try {
			HttpEntity entity = response.getEntity();

			switch (response.getStatusLine().getStatusCode()) {
			case HttpStatus.SC_OK:
				if (TextUtil.isValidate(path)) {
					FileOutputStream fos = new FileOutputStream(path);
					InputStream in = null;
					if (entity.getContentEncoding() != null) {
						String encoding = entity.getContentEncoding().getValue();
						if (encoding != null
								&& "gzip".equalsIgnoreCase(encoding)) {
							in = new GZIPInputStream(entity.getContent());
						} else if (encoding != null
								&& "deflate".equalsIgnoreCase(encoding)) {
							in = new InflaterInputStream(entity.getContent());
						}
					} else {
						in = entity.getContent();
					}
					byte[] b = new byte[IO_BUFFER_SIZE];
					int read;
					while ((read = in.read(b)) != -1) {
						// TODO update progress
						fos.write(b, 0, read);
					}
					fos.flush();
					fos.close();
					in.close();
					return bindData(path);
				} else {
					return bindData(EntityUtils.toString(entity));
				}
			default:
				break;
			}
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Object bindData(String content) {
		return content;
	}

	public AbstractCallback setPath(String path) {
		this.path = path;
		return this;
	}
}
