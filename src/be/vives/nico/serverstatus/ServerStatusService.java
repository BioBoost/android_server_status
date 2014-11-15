package be.vives.nico.serverstatus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServerStatusService extends Service {
    
    private final static String TAG = "ServerStatus.ServerStatusService";
    
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
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
    }
}
