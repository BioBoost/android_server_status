package be.vives.nico.serverstatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static String TAG = "ServerStatusService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	// Start our service
	public void onClickStartService(View V)
	{
		Toast.makeText(this, "Staring Server Status Service", Toast.LENGTH_LONG).show();
		ServerStatusService.startScheduling(this);
		Log.v(TAG, "Started service");
	}
	
	// Stop our service
	public void onClickStopService(View V)
	{
		// We actually cancel the next schedule. Service may be running and will finish
		Toast.makeText(this, "Stopping Server Status Service", Toast.LENGTH_LONG).show();
		ServerStatusService.stopScheduling(this);
		Log.v(TAG, "Stopping Server Status Service");
	}
	
	public void displayTargets(View v) {
		startActivity(new Intent(this, TargetOverviewActivity.class));
	}
	
	public void openPreferences(View view) {
		startActivity(new Intent(this, AppPreferenceActivity.class));
	}
}
