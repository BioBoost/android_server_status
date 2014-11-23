package be.vives.nico.serverstatus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TargetDetails extends Activity {
	
	Target target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_target_details);
		
		// Get the URL
		long targetId = Long.parseLong(getIntent().getData().toString());
		
    	// Fetch the target from the database
		TargetsDataSource doa = new TargetsDataSource(this);
		doa.open();
		this.target = doa.getTarget(targetId);
		doa.close();
		
		populateTheView();
	}
	
	private void populateTheView() {
		((TextView)findViewById(R.id.lblTitle)).setText(this.target.getUri());
		((TextView)findViewById(R.id.txtSuccesses)).setText("" + this.target.getStats().getSuccesses());
		((TextView)findViewById(R.id.txtFails)).setText("" + this.target.getStats().getFails());
		((TextView)findViewById(R.id.txtSubsequentFails)).setText("" + this.target.getStats().getSubsequentFails());
	}
}
