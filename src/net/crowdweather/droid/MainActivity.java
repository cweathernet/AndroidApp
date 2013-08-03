package net.crowdweather.droid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// remove openintents imports for running real device

public class MainActivity extends Activity {

	private GPSTracker gps;
	private double latitude;
	private double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gps = new GPSTracker(MainActivity.this);

		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			// \n is for new line
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		final Button button = (Button) findViewById(R.id.publish);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new getinternetData().execute();

				// getCityFromLocation(37.42273,-122.084198);

			}
		});
	}

	class getinternetData extends AsyncTask<Void, Void, String> {
		

		private static final String TAG = "mainACT";

		@Override
		protected String doInBackground(Void... params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.INSERT_URL);
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("temperature", "30");
				jsonObj.put("humidity", "80%");
				jsonObj.put("pressure", "80%");
				JSONArray locArr  = new JSONArray();
				locArr.put(latitude);
				locArr.put(longitude);
				
//				jsonObj.put("longitude", longitude);
//				jsonObj.put("latitude", latitude);
				jsonObj.put("location",locArr);
				jsonObj.put("date", Calendar.getInstance(Locale.getDefault()).getTime().toString());
				
				JSONArray jsonArry =new  JSONArray();
				jsonArry.put(jsonObj);
				
				nameValuePairs.add(new BasicNameValuePair("docs", jsonArry.toString()));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				Log.d(TAG, httppost.getEntity().getContent().toString());
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				if (statusCode == 200) {
					return "connect success";
				} else
					return "Couldn't connect";
			} catch (Exception e) {
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Toast.makeText(MainActivity.this, "response  : " + result,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void getCityFromLocation(double latitude, double longitude) {
// TODO: integrate google maps api reverse GEO coding.
		try {
			Geocoder c = new Geocoder(this, Locale.getDefault());
			List<Address> l = c.getFromLocation(latitude, longitude, 5);

			for (Address a : l) {

				Log.d("GeocoderTest", "Locality" + a.getLocality()
						+ "Postal Code " + a.getPostalCode() + " (" + a + ")");
				
				// return a.getLocality();
			}

		} catch (IOException e) {
			Log.d("GeocoderTest", "error");
			Log.e("GeocoderTest", "", e);
		}

	}

}
