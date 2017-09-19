package com.httpstest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public static final String URL = "https://192.168.1.137:8443/security-sdk";
	public static final String URL_INIT = URL + "/sdk/init";
	public static final String URL_AUTH = URL + "/api/auth";
	public static InputStream is;
	public static InputStream keystore_is;
	public static InputStream truststore_is;
	private Button btn_get_single;
	private Button btn_post_single;
	private Button btn_get_double;
	private Button btn_post_double;
	private TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			is = new BufferedInputStream(getAssets().open("server.cer"));
			keystore_is = new BufferedInputStream(getAssets().open("client.bks"));
			truststore_is = new BufferedInputStream(getAssets().open("truststore.bks"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		btn_get_single = (Button) findViewById(R.id.https_get_single);
		btn_post_single = (Button) findViewById(R.id.https_post_single);
		btn_get_double = (Button) findViewById(R.id.https_get_double);
		btn_post_double = (Button) findViewById(R.id.https_post_double);
		txt = (TextView) findViewById(R.id.str);
		btn_get_single.setOnClickListener(this);
		btn_post_single.setOnClickListener(this);
		btn_get_double.setOnClickListener(this);
		btn_post_double.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.https_get_single:
			HttpSingle.initSSLContext(is);
			new Thread(new MyRunnable(URL_INIT)).start();
			break;
		case R.id.https_post_single:
			Map<String, String> params = new HashMap<String, String>();
			// 包名
			params.put("packageName", "com.securityplatform");
			// 应用授权key
			params.put("appKey", "ec474652245cbed3eadc79ff8c094c12");
			// idcode
			params.put("idCode", "22");
			new Thread(new MyRunnable(URL_AUTH, params)).start();
			break;
		case R.id.https_get_double:
			try {
				HttpDouble.initHttpsURLConnection("123456", keystore_is, truststore_is);
				new Thread(new MyRunnable(URL_INIT)).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.https_post_double:
			try {
				Map<String, String> param = new HashMap<String, String>();
				// 包名
				param.put("packageName", "com.securityplatform");
				// 应用授权key
				param.put("appKey", "ec474652245cbed3eadc79ff8c094c12");
				// idcode
				param.put("idCode", "22");
				new Thread(new MyRunnable(URL_AUTH, param)).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

	class MyRunnable implements Runnable {

		private String url;

		private Map<String, String> params;
		// 0-get 1-post
		private int type = 0;

		public MyRunnable(String url) {
			this.url = url;
			type = 0;
		}

		public MyRunnable(String url, Map<String, String> param) {
			this.url = url;
			this.params = param;
			type = 1;
		}

		@Override
		public void run() {
			String str = "";
			if (type == 0) {
				str = HTTPSGet.doGet(url);
			} else {
				str = HTTPSPost.post(url, params);
			}
			Message msg = new Message();
			msg.what = 0;
			msg.obj = str;
			handler.sendMessage(msg);
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String content = (String) msg.obj;
			switch (msg.what) {
			case 0:
				txt.setText(content);
				break;
			default:
				break;
			}
		}
	};

}
