package com.lm.lib.http_net_lib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lm.lib.http_net_lib.callback.StringCallback;
import com.lm.lib.http_net_lib.utilities.UrlHelper;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnString;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnString = (Button) findViewById(R.id.btnString);
		btnString.setOnClickListener(this);
		tv = (TextView) findViewById(R.id.textview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnString:
			requestString();
			break;
		}
	}

	private void requestString() {
		Request request = new Request(UrlHelper.test_string_url,
				Request.RequestMethod.GET);
		request.setCallback(new StringCallback() {
			@Override
			public void onSuccess(Object result) {
				tv.setText((String) result);
			}

			@Override
			public void onFailure(Exception result) {

			}
		});
		request.execute();
	}

}
