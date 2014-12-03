package be.vives.nico.serverstatus;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class ServerStatusService extends Service {
    
    private final static String TAG = "ServerStatus.ServerStatusService";
    
    private static Boolean keepRunning = true;
    
    private final static int ALARM_THRESHOLD = 1;	// Should be added to Target class
    
    // Preferences
    private boolean alarm_dovibrate;    
    private boolean alarm_dosms;
    private String alarm_phonenumber;
    private boolean alarm_notification;
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    	Log.v(TAG, "onCreate");
    	
    	// Get preferences and register listener
    	getPreferences();
    }
    
    private void getPreferences() {
    	SharedPreferences appPrefs = getSharedPreferences("be.vives.nico.serverstatus_preferences", MODE_PRIVATE);
    	alarm_dovibrate = appPrefs.getBoolean("alarm_vibrate", false);
    	alarm_dosms = appPrefs.getBoolean("alarm_sentsms", false);
    	alarm_phonenumber = appPrefs.getString("phone_number", "");
    	alarm_notification = appPrefs.getBoolean("alarm_notification", false);
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	// Get the targets from the database
		TargetsDataSource doa = new TargetsDataSource(this);
		doa.open();
		ArrayList<Target> targets = doa.getAllTargets();
		doa.close();

    	// Do the check
    	Log.v(TAG, "Checking the status of the target");
        for (Target target : targets) {            
    		new CheckTargetStatusTask(new IStatusResultReady() {
				@Override
				public void onStatusResultReady(Target target) {
					if (target.getStats().getSubsequentFails() >= ServerStatusService.ALARM_THRESHOLD) {
						if (alarm_dovibrate) {
							ReportTools.vibrate(getApplicationContext(), 2000);
						}
						if (alarm_dosms) {
							ReportTools.sendSMS(alarm_phonenumber, target.getFailedStatusReport());							
						}
						if (alarm_notification) {
							ReportTools.createNotification(getApplicationContext(), target, target.getFailedStatusReport());			
						}
						target.getStats().resetSubsequentFails();
					}

					// Save the target to the database
					TargetsDataSource doa = new TargetsDataSource(getApplicationContext());
					doa.open();
					doa.saveTarget(target);
					doa.close();
				}
			}, this).execute(target);
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
