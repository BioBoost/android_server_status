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
	
	private ArrayList<String> targets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Add targets to list
        populateTargets();
        
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, targets);
        
        // Set The Adapter
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
	
    private void populateTargets()
    {
    	targets = new ArrayList<String>();
    	targets.add("http://www.google.be");
    	targets.add("http://www.labict.be");
    	targets.add("ping:labict.be");
    	targets.add("http://apps.khbo.be");
    	targets.add("ping:apps.khbo.be");
    }
}
