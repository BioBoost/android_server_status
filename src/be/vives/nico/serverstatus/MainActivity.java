package be.vives.nico.serverstatus;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
	
	public void displayStats(View V)
	{
		TableLayout table = (TableLayout)findViewById(R.id.tblStatResults);
		table.removeAllViews();
		
		//Create headers
	    TableRow tableRow = new TableRow(this);

	    TextView t = new TextView(this);
	    t.setText("Target");

	    TextView s = new TextView(this);
	    s.setText("Success");
	    s.setBackgroundColor(Color.GREEN);

	    TextView f = new TextView(this);
	    f.setText("Fails");
	    f.setBackgroundColor(Color.MAGENTA);

	    TextView sf = new TextView(this);
	    sf.setText("Subseq fails");
	    sf.setBackgroundColor(Color.RED);

	    tableRow.addView(t);
	    tableRow.addView(s);
	    tableRow.addView(f);
	    tableRow.addView(sf);
	    table.addView(tableRow);
	    
    	for (Target target : ServerStatusService.targets) {
    	    TableRow tableRow2 = new TableRow(this);

    	    TextView txtTarget = new TextView(this);
    	    txtTarget.setText(target.getUri());

    	    TextView txtSuccess = new TextView(this);
    	    txtSuccess.setText(target.getStats().getSuccesses() + "");
    	    txtSuccess.setBackgroundColor(Color.GREEN);

    	    TextView txtFails = new TextView(this);
    	    txtFails.setText(target.getStats().getFails() + "");
    	    txtFails.setBackgroundColor(Color.MAGENTA);

    	    TextView txtSubsequentFails = new TextView(this);
    	    txtSubsequentFails.setText(target.getStats().getSubsequentFails() + "");
    	    txtSubsequentFails.setBackgroundColor(Color.RED);

    	    tableRow2.addView(txtTarget);
    	    tableRow2.addView(txtSuccess);
    	    tableRow2.addView(txtFails);
    	    tableRow2.addView(txtSubsequentFails);
    	    table.addView(tableRow2);
    	}
	}
}
