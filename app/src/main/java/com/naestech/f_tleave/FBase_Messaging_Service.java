package com.naestech.f_tleave;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import androidx.core.app.NotificationCompat;

/*import com.tripnetra.tnadmin.Notifications.Darshan_Assign_second;
import com.tripnetra.tnadmin.Notifications.Darshan_recycler_first;
import com.tripnetra.tnadmin.R;*/

public class FBase_Messaging_Service extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    String dept_type ,name,from_date,reason,id,to_date;
    SharedPrefs sp;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String Title = remoteMessage.getNotification().getTitle();
        String Message = remoteMessage.getNotification().getBody();
        String Action = remoteMessage.getNotification().getClickAction();
        Log.i("action",Action);

        Log.i("Mess",Message);

      /*  sp = new SharedPrefs(getApplicationContext());
        userid = String.valueOf(sp.getUTypeId());*/

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            id = remoteMessage.getData().get("id");
            name = remoteMessage.getData().get("name");
            from_date = remoteMessage.getData().get("from_date");
            reason = remoteMessage.getData().get("reason");
            to_date = remoteMessage.getData().get("to_date");

              Log.i("e_name", name);
               Log.i("F_Date",from_date);
               Log.i("id",id);
               Log.i("rsn",reason);
               Log.i("to_date",to_date);
        }

        sendNotification(Title, Message, Action, id,name,from_date,reason);

    }

    private void sendNotification(String Title, String Message,String Action,String id,String name,String from_date,String reason) {

        Bundle b = new Bundle();

        b.putString("id",id);
        b.putString("name",name);
        b.putString("from_date",from_date);
        b.putString("reason",reason);

        Log.i("b", String.valueOf(b));


        Intent intent = new Intent(Action);
        intent.putExtras(b);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri Sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id),
                    "Extranettest", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Title);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.notilogo)
                .setContentTitle(Title)
                .setContentText(Message)
                .setAutoCancel(true)
                .setSound(Sound)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(Color.YELLOW, 1000, 300);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        assert notificationManager != null;
        notificationManager.notify(m, builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("tk",s);
    }
}