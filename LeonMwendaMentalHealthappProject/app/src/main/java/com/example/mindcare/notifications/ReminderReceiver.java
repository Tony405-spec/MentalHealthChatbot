package com.example.mindcare.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.mindcare.R;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "mindcare_reminders";

    @Override public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "MindCare reminders", NotificationManager.IMPORTANCE_DEFAULT));
        }
        String type = intent.getStringExtra("type");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mindcare)
                .setContentTitle(type == null ? "MindCare reminder" : type + " reminder")
                .setContentText("Take a gentle moment for your wellbeing.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
