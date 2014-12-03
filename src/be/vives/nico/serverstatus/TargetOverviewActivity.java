package be.vives.nico.serverstatus;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import be.vives.nico.serverstatus.TargetDetails;

public class TargetOverviewActivity extends ListActivity {

	//private ArrayList<String> targets;
	private ArrayList<Target> targets;
	
	// Selected target for context menu
	private Target selectedTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Add targets to list
        populateTargets();
        
        // Register context menu for listview
        this.registerForContextMenu(getListView());
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
	            startActivity(new Intent(this, SaveTargetActivity.class));
	            return true;
	        case R.id.delete_all_targets:
	        	// Ask user if he's sure
	        	new AlertDialog.Builder(this)
	            .setIcon(android.R.drawable.ic_dialog_alert)
	            .setTitle("Deleting All Targets")
	            .setMessage("Are you sure you want to delete all targets from the database?")
	            .setPositiveButton("Yes I Am", new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
			            Log.v("Menu", "Deleting all targets from db");
			            
			    		TargetsDataSource doa = new TargetsDataSource(getBaseContext());
			    		doa.open();
			    		doa.deleteAllTargets();		// Remove all existing records
			    		doa.close();
			    		
			    		// Refresh list
			            populateTargets();
		            }
		        })
		        .setNegativeButton("No", null)
		        .show();
	            return true;
	        case R.id.refresh_targets:
	            Log.v("Menu", "Refreshing target overview");
	            populateTargets();
	            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.target_context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get info on selected target
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        this.selectedTarget = targets.get(info.position);
        switch (item.getItemId()) {
            case R.id.edit_target:                
                // Launch save target activity with id as data
                Intent intent = new Intent(this, SaveTargetActivity.class);
                intent.setData(Uri.parse(selectedTarget.getId() + ""));
                startActivity(intent);
                
                return true;
            case R.id.delete_target:
            	
	        	// Ask user if he's sure
	        	new AlertDialog.Builder(this)
	            .setIcon(android.R.drawable.ic_dialog_alert)
	            .setTitle("Deleting Target")
	            .setMessage("Are you sure you want to delete the target " + selectedTarget.getUri() + " from the database?")
	            .setPositiveButton("Yes I Am", new DialogInterface.OnClickListener()
		        {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
			            Log.v("Menu", "Deleting target " + selectedTarget.getUri() + " from db");
		            	
			            // Remove target from the database
			    		TargetsDataSource doa = new TargetsDataSource(getBaseContext());
			    		doa.open();
			    		doa.deleteTarget(selectedTarget);
			    		doa.close();
		                
		                Toast.makeText(getApplicationContext(), "Deleted target " + selectedTarget.getUri(), Toast.LENGTH_SHORT).show();
		                
		                // Re-populate listview
		                populateTargets();
			            
			            // Reset selected target to null
			            selectedTarget = null;
		            }
		        })
		        .setNegativeButton("No", null)
		        .show();
	        	
                return true;
            case R.id.reset_stats:
            	
	            Log.v("Menu", "Resetting stats of target " + selectedTarget.getUri());
            	
	            // Reset stats
	    		TargetsDataSource doa = new TargetsDataSource(getBaseContext());
	    		doa.open();
	    		selectedTarget.getStats().clearStats();
	    		doa.saveTarget(selectedTarget);
	    		selectedTarget = null;
	    		doa.close();
	    		
                // Re-populate listview
                populateTargets();
	        	
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
