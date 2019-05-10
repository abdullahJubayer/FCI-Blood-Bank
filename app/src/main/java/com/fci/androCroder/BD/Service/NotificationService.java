package com.fci.androCroder.BD.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.fci.androCroder.BD.Post_activity;
import com.fci.androCroder.BD.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.Random;

public class NotificationService  extends FirebaseMessagingService {

    private static final String Channel_Id="Notification channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String,String> root=remoteMessage.getData();
        Log.e("data",root.toString());
        String message=root.get("Message");
        Random random=new Random(100);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent intent=PendingIntent.getActivity(this,0,new Intent(this, Post_activity.class),0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Channel_Id);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("FCI Blood Bank")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(uri)
                ;

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(Channel_Id,"Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Description");
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(random.nextInt(), builder.build());

    }

}
