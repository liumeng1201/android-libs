package com.lm.lib.http_net_lib;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lm.lib.http_net_lib.callback.JsonCallback;
import com.lm.lib.http_net_lib.callback.PathCallback;
import com.lm.lib.http_net_lib.callback.StringCallback;
import com.lm.lib.http_net_lib.entities.Entity;
import com.lm.lib.http_net_lib.interfaces.IProgressListener;
import com.lm.lib.http_net_lib.net.Request;
import com.lm.lib.http_net_lib.utilities.UploadUtil;
import com.lm.lib.http_net_lib.utilities.UrlHelper;
import com.lm.lib.http_net_lib.utilities.UserInfo;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnString, btnJson;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnString = (Button) findViewById(R.id.btnString);
		btnString.setOnClickListener(this);
		btnJson = (Button) findViewById(R.id.btnJson);
		btnJson.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.textview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnString:
			requestString();
			break;
		case R.id.btnJson:
			// requestJson();
			testCancel();
			break;
		}
	}

	private void requestJson(final String filePath) {
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + "test.txt";

		Request request = new Request(UrlHelper.test_json_url,
				Request.RequestMethod.GET);
		request.setCallback(new JsonCallback<ArrayList<Entity>>() {
			@Override
			public void onSuccess(ArrayList<Entity> result) {
				for (Entity entity : result) {
					tv.append(entity.city + "\n");
				}
			}

			@Override
			public void onFailure(AppException result) {
				result.getErrorInfo().getInt(UserInfo.CLOUD_RESPONSE_STATUS_CODE);
				result.printStackTrace();
			}

			@Override
			public ArrayList<Entity> onPreHandle(ArrayList<Entity> object) {
				// 将数据插入数据库
				return super.onPreHandle(object);
			}

			@Override
			public ArrayList<Entity> onPreRequest() {
				// 查询数据库是否已有数据
				return super.onPreRequest();
			}

			@Override
			public boolean onPrepareParams(OutputStream out)
					throws AppException {
				UploadUtil.upload(out, filePath);
				return super.onPrepareParams(out);
			}
			
		}.setReturnType(new TypeToken<ArrayList<Entity>>() {
		}.getType()).setPath(path));
		request.setProgressListener(new IProgressListener() {
			@Override
			public void onProgressUpdate(int curPos, int contentLength) {
				System.out.println("currentPosition = " + curPos
						+ ", contentLength = " + contentLength);
			}
		});
		request.execute();
	}

	private void testCancel() {
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + "test.jpg";

		final Request request = new Request(
				"http://c.hiphotos.baidu.com/image/w%3D2048%3Bq%3D90/sign=609e52020ed79123e0e09374990c62f3/962bd40735fae6cdd197a96b0db30f2442a70f9c.jpg",
				Request.RequestMethod.GET);
		request.setCallback(new PathCallback() {
			@Override
			public void onSuccess(String result) {
			}

			@Override
			public void onFailure(AppException result) {
				result.printStackTrace();
			}
		}.setPath(path));
		request.setProgressListener(new IProgressListener() {
			@Override
			public void onProgressUpdate(int curPos, int contentLength) {
				System.out.println("currentPosition = " + curPos
						+ ", contentLength = " + contentLength);
				if (curPos > 2) {
					request.cancel();
				}
			}
		});
		request.execute();
	}

	private void requestString() {
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + "test.txt";
		Request request = new Request(UrlHelper.test_string_url,
				Request.RequestMethod.GET);
		request.setCallback(new StringCallback() {
			@Override
			public void onSuccess(Object result) {
				tv.setText((String) result);
			}

			@Override
			public void onFailure(AppException result) {
				result.printStackTrace();
			}
		}.setPath(path));
		request.execute();
	}

}
