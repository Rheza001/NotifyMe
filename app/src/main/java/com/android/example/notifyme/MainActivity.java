package com.android.example.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
  private Button mNotifyButton, mUpdateButton, mCancelButton;
  private NotificationManager mNotifyManager;
  private static final int NOTIFICATION_ID = 0;
  private String NOTIFICATION_GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html";
  private static final String ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
  BroadcastReceiver mReceiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mNotifyButton = (Button) findViewById(R.id.notify);
    mNotifyButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendNotification();
      }
    });

    mUpdateButton = (Button) findViewById(R.id.update);
    mUpdateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateNotification();
      }
    });

    mCancelButton = (Button) findViewById(R.id.cancel);
    mCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancelNotification();
      }
    });

    registerReceiver(mReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));

    mNotifyButton.setEnabled(true);
    mUpdateButton.setEnabled(false);
    mCancelButton.setEnabled(false);

    mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
  }

  public void sendNotification(){
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_GUIDE_URL));
    PendingIntent learnMorePendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

    Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
    PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
            NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

    NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .addAction(R.drawable.ic_learn_more, "Learn More", learnMorePendingIntent)
            .addAction(R.drawable.ic_update, "Update", updatePendingIntent);

    Notification myNotification = notifyBuilder.build();
    mNotifyManager.notify(NOTIFICATION_ID, myNotification);

    mNotifyButton.setEnabled(false);
    mUpdateButton.setEnabled(true);
    mCancelButton.setEnabled(true);

  }

  public void updateNotification(){
    Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);

    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_GUIDE_URL));
    PendingIntent learnMorePendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

    NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notification Updated!"))
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .addAction(R.drawable.ic_learn_more, "Learn More", learnMorePendingIntent);
    mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    mNotifyButton.setEnabled(false);
    mUpdateButton.setEnabled(false);
    mCancelButton.setEnabled(true);
  }

  public void cancelNotification(){
    mNotifyManager.cancel(NOTIFICATION_ID);

    mNotifyButton.setEnabled(true);
    mUpdateButton.setEnabled(false);
    mCancelButton.setEnabled(false);
  }

  @Override
  protected void onDestroy() {
    unregisterReceiver(mReceiver);
    super.onDestroy();
  }

  public class NotificationReceiver extends BroadcastReceiver{
    BroadcastReceiver mReceiver;

    public NotificationReceiver(BroadcastReceiver mReceiver) {
      this.mReceiver = mReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
      updateNotification();
    }
  }
}
