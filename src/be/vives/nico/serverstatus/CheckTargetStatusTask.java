package be.vives.nico.serverstatus;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class CheckTargetStatusTask extends AsyncTask<Target, Integer, Target> {
    
    private final static String TAG = "CheckTargetStatusTask";
    
    // Optional callback for when ping check is ready
    private IStatusResultReady listener;
    
    // Context is needed for internet check
    private Context context;

    public CheckTargetStatusTask(IStatusResultReady listener, Context context) {
    	this(context);
        this.listener = listener;
    }

    public CheckTargetStatusTask(Context context) {
    	this.context = context;
    }
    
    // Do the long-running work in here
    protected Target doInBackground(Target... targets) {
    	Target target = targets[0];
    	
        Log.v(TAG, "Starting Status Check for " + target.getUri());
        Boolean success = false;
        
        // Check if internet connection available
        if (NetworkTools.isNetworkAvailable(this.context)) {
            // Do the actual check
            try {
            	success = target.doStatusCheck();
            } catch (Exception e) {
                Log.d(TAG, "Exception: " + e);
            }
            
            if (success) {
                target.getStats().incrementSuccesses();
            } else {
            	target.getStats().incrementFails();
            }
        }
            
        return target;
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Target target) {
        Log.v(TAG, "Result: " + target.getUri() + " => " + target.getStats());
        if (this.listener != null) {
            listener.onStatusResultReady(target);
        }
    }
}
