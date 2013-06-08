package org.homemarket.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MarketActivity extends Activity {
	public String getStringContent(String uri) throws Exception {
		URL url = new URL(uri);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		try {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader buf = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String s;
			while (true) {
				s = buf.readLine();
				if (s == null || s.length() == 0)
					break;
				sb.append(s);
			}
			buf.close();
			in.close();
			return sb.toString();
		} finally {
			urlConnection.disconnect();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String body;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			TextView textView;
			textView = new TextView(this);
			
			if (networkInfo != null && networkInfo.isConnected()) {
				body = getStringContent("http://echo.jsontest.com/key/value/one/two");
				textView.setTextSize(40);
				textView.setText(body);
				setContentView(textView);
			} else {
				textView.setTextSize(40);
				textView.setText("Sem conexão à internet");
				setContentView(textView);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.market, menu);
		return true;
	}

}
