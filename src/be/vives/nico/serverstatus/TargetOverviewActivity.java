package be.vives.nico.serverstatus;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import be.vives.nico.serverstatus.TargetDetails;

public class TargetOverviewActivity extends ListActivity {

	//private ArrayList<String> targets;
	private ArrayList<Target> targets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Add targets to list
        populateTargets();
	}
	
    private void populateTargets()
    {
    	// First get the targets from the database
		TargetsDataSource doa = new TargetsDataSource(this);
		doa.open();
		this.targets = doa.getAllTargets();
		doa.close();
		
        // Create custom Target adapter
        ArrayAdapter<Target> arrayAdapter = new TargetArrayAdapter(this, targets);
        
        // Set the adapter for the ListView
        ListView lv = getListView();
        lv.setAdapter(arrayAdapter); 
        
        // Register onClickListener to handle click events on each item
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	// Get the selected target id
                long targetId = targets.get(position).getId();
            	
            	// Create an intent with the ID of the target as data
            	Intent intent = new Intent(view.getContext(), TargetDetails.class);
            	intent.setData(Uri.parse(String.valueOf(targetId)));

            	// Launch the target detail activity
            	startActivity(intent);
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.target_overview_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_target:
                Log.v("Menu", "Creating new target");
                startActivity(new Intent(this, CreateTargetActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
