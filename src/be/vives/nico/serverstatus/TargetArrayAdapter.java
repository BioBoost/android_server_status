package be.vives.nico.serverstatus;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TargetArrayAdapter extends ArrayAdapter<Target> {
	public TargetArrayAdapter(Context context, ArrayList<Target> targets) {
		super(context, 0, targets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Target target = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.target_overview_row, parent, false);
		}

		// Populate the data into the template view using the data object
		((TextView)convertView.findViewById(R.id.target)).setText(target.getUri());
		((TextView)convertView.findViewById(R.id.txtSuccesses)).setText("" + target.getStats().getSuccesses());
		((TextView)convertView.findViewById(R.id.txtFails)).setText("" + target.getStats().getFails());
		((TextView)convertView.findViewById(R.id.txtSubsequentFails)).setText("" + target.getStats().getSubsequentFails());
		
		// Return the completed view to render on screen
		return convertView;
	}
}
