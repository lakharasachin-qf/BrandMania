package com.app.brandmania.Connection;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("notificationId");
        /* Your code to handle the event here */
        if (notificationId == 108) {
            Log.e("Service", "Emerygency call");
            NotificationManager notify_manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notify_manager.cancel(108);
            notify_manager.cancelAll();

        }
    }
}