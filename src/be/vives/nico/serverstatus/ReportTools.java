package be.vives.nico.serverstatus;

import android.content.Context;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

public class ReportTools {
	
	private final static String TAG = "ServerStatusService.ReportTools";
	
    public static void sendSMS(String phoneNumber, String message)
    {
        Log.v(TAG, "Sending sms: " + message);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    
    public static void vibrate(Context context, int milliseconds)
    {
        Log.v(TAG, "Vibrating");
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(milliseconds);
    }
}
