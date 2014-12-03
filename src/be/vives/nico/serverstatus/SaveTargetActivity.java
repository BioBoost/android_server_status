package be.vives.nico.serverstatus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

public class SaveTargetActivity extends Activity {
	
	private Target target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_target);
		
        // First we check if id is set. If it is we are actually editing existing
        // target in db.
        if (getIntent().getData() != null) {
            // Get the ID
            long targetId = Long.parseLong(getIntent().getData().toString());
            
            // Fetch the target from the database
            TargetsDataSource doa = new TargetsDataSource(this);
            doa.open();
            this.target = doa.getTarget(targetId);
            doa.close();
            
            populateView();
        } else {
            this.target = null;
        }
	}
	
    private void populateView() {
        ((TextView)findViewById(R.id.txtTargetUri)).setText(this.target.getUri());

        // Get the type selection spinner
        Spinner spinner = ((Spinner)findViewById(R.id.lstTargetType));
        
         // TODO Refactor code below which is really bad
        if (this.target.getClass().getSimpleName().equals("TargetHost")) {
        	spinner.setSelection(getSpinnerValueIndex(spinner, "Host"));
        } else if (this.target.getClass().getSimpleName().equals("TargetSite")) {
        	spinner.setSelection(getSpinnerValueIndex(spinner, "Website"));
        }
    }
    
    private int getSpinnerValueIndex(Spinner spinner, String value) {
        int i = 0;
        while (i < spinner.getCount() &&
                !spinner.getItemAtPosition(i).equals(value)) {
            i++;
        }
        if (i < spinner.getCount()) {
            return i;
        } else {
            return -1;
        }
    }
	
	public void onSaveTarget(View v) {

		// Get target info
		String uri = ((EditText)findViewById(R.id.txtTargetUri)).getText().toString();
		String type = ((Spinner)findViewById(R.id.lstTargetType)).getSelectedItem().toString();
		
        // Create target
        Target newTarget = null;
        if (type.equals("Host")) {
            newTarget = new TargetHost(uri);
        } else if (type.equals("Website")) {
            newTarget = new TargetSite(uri);
        }
		
        if (newTarget != null) {
            // Open db connection
            TargetsDataSource doa = new TargetsDataSource(this);
            doa.open();

            Target result = null;

            // If editing target
            if (this.target != null) {
                // We need to copy the stats if we are editing target
            	// (user can choose to reset them via overview context menu)
                newTarget.getStats().setStats(
                        target.getStats().getSuccesses(),
                        target.getStats().getFails(),
                        target.getStats().getSubsequentFails());

                // Recover id
                newTarget.setId(this.target.getId());
                result = doa.saveTarget(newTarget);
            } else {
                result = doa.insertTarget(newTarget);
            }

            if (result != null) {
                Log.v("DATABASE", "Saved record with id = " + result.getId());
                Toast.makeText(getApplicationContext(), "Target " + uri + " saved successfully",
                        Toast.LENGTH_LONG).show();
                // Go back
                finish();
            } else {
                Log.d("DATABASE", "Failed to save record");
                Toast.makeText(getApplicationContext(), "Could not save target " + uri,
                        Toast.LENGTH_LONG).show();
            }
            doa.close();
        }
	}
}
