package net.crowdweather.droid;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// remove openintents imports for running real device

public class MainActivity extends Activity {

	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		   gps = new GPSTracker(MainActivity.this);
		  
        if(gps.canGetLocation()){
            
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
             
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

		final Button button = (Button) findViewById(R.id.publish);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new getinternetData().execute();

			}
		});
	}

	class getinternetData extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Constants.API_BASE_URL);
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs.add(new BasicNameValuePair("temperature", "30"));
				nameValuePairs.add(new BasicNameValuePair("humidity",
						"80%"));
				nameValuePairs.add(new BasicNameValuePair("pressure",
						"80%"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				// message.setText("444");
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
						Toast.LENGTH_LONG
						).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
