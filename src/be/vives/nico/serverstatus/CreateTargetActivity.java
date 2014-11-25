package be.vives.nico.serverstatus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateTargetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_target);
	}
	
	public void onCreateTarget(View v) {

		// Get target info
		String uri = ((EditText)findViewById(R.id.txtTargetUri)).getText().toString();
		String type = ((Spinner)findViewById(R.id.lstTargetType)).getSelectedItem().toString();
		
		// Create target
		Target target = null;
		if (type.equals("Host")) {
			target = new TargetHost(uri);
		} else if (type.equals("Website")) {
			target = new TargetSite(uri);
		}
		
		if (target != null) {
			// Add target to database		
			TargetsDataSource doa = new TargetsDataSource(this);
			doa.open();
			
			Target result = doa.insertTarget(target);
			
			if (result != null) {
				Log.v("DATABASE", "Created record with id = " + result.getId());
				Toast.makeText(getApplicationContext(), "Target " + uri + " created successfully",
						Toast.LENGTH_LONG).show();
				// Go back
				finish();
			} else {
				Log.d("DATABASE", "Failed to create record");
				Toast.makeText(getApplicationContext(), "Could not create target " + uri,
						Toast.LENGTH_LONG).show();
			}
			doa.close();
		}
	}
}
