package be.vives.nico.serverstatus;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class TargetSite extends Target {

	public TargetSite(String uri) {
		super(uri);
	}

	@Override
	Boolean doStatusCheck() throws Exception {
		Boolean result = false;
		
		// Do a HTTP request to the server
		HttpGet httpRequest = new HttpGet(this.getUri());
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httpRequest);
		
		// Check the response status
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			result = true;
			Log.v("TargetSite", this.getUri() + " responded with status 200 OK");
		} else {
			Log.v("TargetSite", this.getUri() + " responded with status " + response.getStatusLine().getStatusCode());
		}
		
		return result;
	}
	
	@Override
	public String getFailedStatusReport() {
		return this.getUri() + " failed to respond " + this.getStats().getSubsequentFails()
				+ " times in a row to a HTTP request.";
	}
}
