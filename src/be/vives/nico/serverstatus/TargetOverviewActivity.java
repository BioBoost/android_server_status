package be.vives.nico.serverstatus;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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
            	// Get the selected item
                //String selectedItem = targets.get(position);
                
            	// Launch the target detail activity
            	startActivity(new Intent(view.getContext(), TargetDetails.class));
            }
        });
    }
}
