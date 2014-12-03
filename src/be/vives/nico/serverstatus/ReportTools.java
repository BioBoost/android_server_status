package be.vives.nico.serverstatus;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    
    public static void createNotification(Context context, Target target, String message) {		
		// Build intent for notification content
		Intent viewIntent = new Intent(context, TargetDetails.class);
		viewIntent.setData(Uri.parse(target.getId() + ""));
		PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0, viewIntent, 0);

		NotificationCompat.Builder notificationBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("Server Status - " + target.getUri())
		        .setContentText(message)
		        .setContentIntent(viewPendingIntent);
		
		// 100ms delay, vibrate for 250ms, pause for 100 ms and then vibrate for 500ms
		notificationBuilder.setVibrate(new long[] { 100, 250, 100, 500});

		// Get an instance of the NotificationManager service
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		// Build the notification and issues it with notification manager
		// We use target id as notification id
		notificationManager.notify((int)target.getId(), notificationBuilder.build());
    }
}
