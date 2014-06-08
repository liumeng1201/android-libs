package com.lm.lib.http_net_lib.callback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.lm.lib.http_net_lib.AppException;
import com.lm.lib.http_net_lib.Request;
import com.lm.lib.http_net_lib.AppException.EnumException;
import com.lm.lib.http_net_lib.interfaces.ICallback;
import com.lm.lib.http_net_lib.interfaces.IProgressListener;
import com.lm.lib.http_net_lib.utilities.TextUtil;

public abstract class AbstractCallback<T> implements ICallback<T> {
	public static final int IO_BUFFER_SIZE = 4 * 1024;
	public Class<T> returnClass;
	public Type returnType;
	public String path;
	
	protected boolean isCancelled;
	
	@Override
	public void checkIfCancelled() throws AppException {
		if (isCancelled) {
			throw new AppException(EnumException.CancelException, "request has been cancelled");
		}
	}

	public T handle(HttpResponse response, IProgressListener mProgressListener) throws AppException {
		// file, json, xml, image, string
		checkIfCancelled();
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
					long currentPos = 0;
					long length = entity.getContentLength();
					while ((read = in.read(b)) != -1) {
						checkIfCancelled();
						if (mProgressListener != null) {
							currentPos += read;
							mProgressListener.onProgressUpdate(
									(int) (currentPos / 1024),
									(int) (length / 1024));
						}
						fos.write(b, 0, read);
					}
					fos.flush();
					fos.close();
					in.close();
					return bindData(path);
				} else {
					return bindData(EntityUtils.toString(entity, Request.ENCODING));
				}
			default:
				break;
			}
			return null;
		} catch (ParseException e) {
			throw new AppException(EnumException.ParseException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}

	protected T bindData(String content) throws AppException {
		checkIfCancelled();
		return null;
	}

	public AbstractCallback<T> setPath(String path) {
		this.path = path;
		return this;
	}
	
	public AbstractCallback<T> setReturnClass(Class<T> returnClass) {
		this.returnClass = returnClass;
		return this;
	}
	
	public AbstractCallback<T> setReturnType(Type type) {
		this.returnType = type;
		return this;
	}
	
	public void cancel() {
		isCancelled = true;
	}
}
