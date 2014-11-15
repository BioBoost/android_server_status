package be.vives.nico.serverstatus;

import android.os.AsyncTask;
import android.util.Log;

public class CheckTargetStatusTask extends AsyncTask<Target, Integer, Target> {
    
    private final static String TAG = "ServerStatusService.PingTask";
    
    // Optional callback for when ping check is ready
    private IStatusResultReady listener;

    public CheckTargetStatusTask(IStatusResultReady listener) {
        this.listener = listener;
    }

    public CheckTargetStatusTask() {
    }
    
    // Do the long-running work in here
    protected Target doInBackground(Target... targets) {
    	Target target = targets[0];
    	
        Log.v(TAG, "Starting Status Check for " + target.getUri());
        Boolean success = false;
        
        // Do the actual ping
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
