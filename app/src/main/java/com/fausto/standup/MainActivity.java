package com.fausto.standup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
  //  alarmToggle;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID= "primary_notification_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

       final ToggleButton alarmToggle = findViewById(R.id.alarmtoggle);

        final Intent notifyIntent = new Intent(this,AlarmReceiver.class);

        boolean alarmUp= (PendingIntent.getBroadcast(MainActivity.this, NOTIFICATION_ID, notifyIntent,PendingIntent.FLAG_NO_CREATE)!= null);

        alarmToggle.setChecked(alarmUp);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID, notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


     alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
             String toastMensaje;

             if(isChecked){

                // deliverNotification(MainActivity.this);
               //  long prueba = 1;

         long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
         long triggerTime = SystemClock.elapsedRealtime()+ repeatInterval;

         if(alarmManager != null){
             alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, triggerTime,repeatInterval, notifyPendingIntent);
         }

                 toastMensaje = "Stand Up Alarm On!";

             }else{

                 if(alarmManager != null){
                     alarmManager.cancel(notifyPendingIntent);
                 }
                 mNotificationManager.cancelAll();
                 toastMensaje = "Stand Up Alarm Off";
             }
             Toast.makeText(MainActivity.this,toastMensaje,Toast.LENGTH_SHORT).show();

         }
     });

     createNotificationChannel();

    }

    public void createNotificationChannel(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ){

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"Stand Up notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);

        }
    }

   /* public void deliverNotification(Context context){
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPedingItent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stand_up)
                .setContentTitle("Stand Up Alert")
                .setContentText("Deverias ponerte de pie y caminar ahora!")
                .setContentIntent(contentPedingItent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }*/
}