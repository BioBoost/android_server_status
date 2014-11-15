package be.vives.nico.serverstatus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServerStatusService extends Service {
    
    private final static String TAG = "ServerStatus.ServerStatusService";
    
    private static Boolean keepRunning = true;
    
    // Currently targets are hardcoded. Currently have no idea
    // on how to get the data to this task from outside.
    // Later should be fetched from DB
    public final static Target targets[] = {
        new TargetHost("apps.khbo.be"),
        new TargetHost("www.labict.be"),
        new TargetHost("www.amazon.com"),
        new TargetHost("www.vives.be")
    };
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    	Log.v(TAG, "onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	Log.v(TAG, "onStart");

    	// Do the check
    	Log.v(TAG, "Checking the status of the target");
        for (Target target : targets) {
            new CheckTargetStatusTask().execute(target);
        }
    	
        // We don't want this service to stay in memory, so we stop it here.
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        
        // Restart the service in a minute
        if (ServerStatusService.keepRunning) {
            this.scheduleService(60);
        } else {
            Log.v(TAG, "Not rescheduling status check");
        }
    }
    
    public void scheduleService(int seconds) {
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
            // This alarm will wake up the device when System.currentTimeMillis()
            // equals the second argument value
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + (1000 * seconds), // One minute from now
            // PendingIntent.getService creates an Intent that will start a service
            // when it is called. The first argument is the Context that will be used
            // when delivering this intent. Using this has worked for me. The second
            // argument is a request code. You can use this code to cancel the
            // pending intent if you need to. Third is the intent you want to
            // trigger. In this case I want to create an intent that will start my
            // service. Lastly you can optionally pass flags.
            PendingIntent.getService(this, 0, new Intent(this, ServerStatusService.class), 0)
        );
        Log.v(TAG, "Scheduling status check in " + seconds + " seconds");
    }
    
    public static void stopScheduling(Context context) {
    	ServerStatusService.keepRunning = false;
		AlarmManager alarm = (AlarmManager)context.getSystemService(ALARM_SERVICE);
		alarm.cancel(PendingIntent.getService(context, 0, new Intent(context, ServerStatusService.class), 0));
    }
    
    public static void startScheduling(Context context) {
    	ServerStatusService.keepRunning = true;
		context.startService(new Intent(context, ServerStatusService.class));
    }
}
