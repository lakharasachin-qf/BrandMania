package com.make.mybrand.Connection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.make.mybrand.Activity.HomeActivity;
import com.make.mybrand.Activity.basics.SpleshActivity;
import com.make.mybrand.Activity.brand.ViewBrandActivity;
import com.make.mybrand.Activity.details.ImageCategoryDetailActivity;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.R;
import com.make.mybrand.utils.CodeReUse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String GROUP_KEY = "com.make.mybrand.FESTIVALS";

    private String catName;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().containsKey("cat_name")) {
            catName = remoteMessage.getData().get("cat_name");
        }

        shownotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("msg"), remoteMessage.getData().get("flag"), remoteMessage.getData().get("image"), remoteMessage.getData().get("cat_id"));
    }


    private void shownotification(String title, String msg, String message, String url, String cat_id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CodeReUse.CHANNEL_ID, CodeReUse.CHANNEL_NAME, importance);
            mChannel.setDescription(CodeReUse.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        String copiedMessage = message;
        Intent intent = new Intent(this, HomeActivity.class);
        PreafManager preafManager = new PreafManager(this);
        if (preafManager.getUserToken() != null && !preafManager.getUserToken().isEmpty()) {
            if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addBrand")) {
                intent = new Intent(this, ViewBrandActivity.class);
            } else if (copiedMessage != null && copiedMessage.equalsIgnoreCase("addFrame")) {
                intent = new Intent(this, ViewBrandActivity.class);
            } else {
                if (cat_id == null || cat_id.equals("0"))
                    intent = new Intent(this, HomeActivity.class);
                else {
                    intent = new Intent(this, ImageCategoryDetailActivity.class);
                    intent.putExtra("notification", "1");
                    intent.putExtra("cat_id", cat_id);
                    intent.putExtra("catName", catName);
                }
            }
        } else {
            intent = new Intent(this, SpleshActivity.class);
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getActivity(
                    this,
                    0, intent,
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(
                    this,
                    0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CodeReUse.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.ic_launcher_icon))
                .setContentTitle(title)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri)
                .setGroup(GROUP_KEY)
                .setDeleteIntent(createOnDismissedIntent(this))
                .setContentIntent(pendingIntent);

        notificationBuilder.setContentText(msg);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            notificationBuilder.setContentText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
//            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT)));
//        } else {
//            notificationBuilder.setContentText(Html.fromHtml(msg));
//            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(msg)));
//        }

        if (url != null && !url.isEmpty()) {
            Bitmap bitmap = getBitmapfromUrl(url);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
        }

        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_icon);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify("Brand Mania", (int) System.currentTimeMillis(), notificationBuilder.build());
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.putExtra("notificationId", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 108, intent, 0);
        return pendingIntent;
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            return null;
        }
    }
}


