package be.vives.nico.serverstatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private final static String TAG = "ServerStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	// Start our service
	public void onClickStartService(View V)
	{
		startService(new Intent(this, ServerStatusService.class));
		Log.v(TAG, "Started service");
	}
	
	// Stop our service
	public void onClickStopService(View V)
	{
		stopService(new Intent(this, ServerStatusService.class));
		Log.v(TAG, "Stopped service");
	}
}
