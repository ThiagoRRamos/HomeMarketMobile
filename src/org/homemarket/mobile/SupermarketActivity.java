package org.homemarket.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SupermarketActivity extends Activity {

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
		setContentView(R.layout.activity_supermarket);

		String body;
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		TextView textView;
		textView = new TextView(this);
		ArrayAdapter<String> adapter = null;
		try {
			if (networkInfo != null && networkInfo.isConnected()) {
				body = getStringContent("http://pacific-chamber-4578.herokuapp.com/json/supermercado/");
				textView.setTextSize(40);
				JSONObject jsonObjects;
				jsonObjects = new JSONObject(body);
				JSONArray supermercados = (JSONArray) jsonObjects
						.get("supermercados");
				String[] markets = new String[supermercados.length()];
				for (int i = 0; i < supermercados.length(); i++) {
					JSONObject jsonOb = supermercados.getJSONObject(i);
					markets[i] = jsonOb.get("nome_exibicao").toString();
				}
				adapter = new ArrayAdapter<String>(this, R.layout.simple_row,
						markets);

			} else {
				textView.setTextSize(40);
				textView.setText("Sem conexão à internet");
				setContentView(textView);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ListView listSuper = (ListView) findViewById(R.id.listView);
		listSuper.setAdapter(adapter);

		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
