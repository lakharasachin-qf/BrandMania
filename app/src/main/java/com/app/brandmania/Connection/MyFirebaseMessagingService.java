package com.app.brandmania.Connection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.ViewAllImage;
import com.app.brandmania.Activity.ViewBrandActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        Log.e("NEW_TOKEN",s);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Log.e("remoteMessage", remoteMessage.getData().toString());
//
//        // Checking for first time launch - before calling setContentView()
//        PreafManager prefManager = new PreafManager(getApplicationContext());
//
//        //if (prefManager.getReceiveNotification()) {
//        ///         removePendingOrders(remoteMessage.getData().get("message"));
//        Utility.Log("Notification", remoteMessage.getData().get("title") + "-" + remoteMessage.getData().get("msg") + "-" + remoteMessage.getData().get("flag"));
//
//
//    }

    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived( RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("remoteMessage", remoteMessage.getData().toString());

        // Checking for first time launch - before calling setContentView()
        PreafManager prefManager = new PreafManager(getApplicationContext());

        //if (prefManager.getReceiveNotification()) {
        ///         removePendingOrders(remoteMessage.getData().get("message"));
        Utility.Log("Notification", remoteMessage.getData().get("title") + "-" + remoteMessage.getData().get("msg") + "-" + remoteMessage.getData().get("flag"));

        //Utility.Log("Notification", remoteMessage.getNotification().getTitle() + "- " + remoteMessage.getNotification().getBody()+"-"+remoteMessage.getNotification().);
        shownotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("msg"), remoteMessage.getData().get("flag"));
        //shownotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        //  }

    }


    private void shownotification(String title, String msg, String message) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CodeReUse.CHANNEL_ID, CodeReUse.CHANNEL_NAME, importance);
            mChannel.setDescription(CodeReUse.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            //mChannel.setSound(soundUri, audioAttributes);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        String copiedMessage = message;
        Intent intent = new Intent(this, HomeActivity.class);;
        if (copiedMessage.equalsIgnoreCase("addBrand")) {
            intent = new Intent(this, ViewBrandActivity.class);

        } else if (copiedMessage.equalsIgnoreCase("addFrame")) {
            intent = new Intent(this, ViewBrandActivity.class);
        }
        else {
        intent = new Intent(this, HomeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CodeReUse.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_launcher_icon))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_icon);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_icon);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

}


