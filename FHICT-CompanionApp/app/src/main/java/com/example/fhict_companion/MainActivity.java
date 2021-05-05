package com.example.fhict_companion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity implements TokenFragment.OnFragmentInteractionListener {

    static String token;
    ImageButton ibNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        setToolbar();

        TokenFragment tokenFragment = new TokenFragment();
        if(getIntent().getStringExtra("openFragment") != null){
            Bundle bundle = new Bundle();
            bundle.putString("openFragment", getIntent().getStringExtra("openFragment"));
            tokenFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tokenFragment).commit();
    }

    @Override
    public void onFragmentInteraction(String token) {
        MainActivity.token = token;
    }

    public void setToolbar() {
        ibNotification = findViewById(R.id.ibReceiveNotification);
        ibNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveNotification();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "channel";
        String description = "notification channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("1", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void receiveNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("openFragment", "building");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Fontys is open!")
                .setContentText("Click here to see the open Fontys buildings!")
                .setChannelId("1")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        notificationManager.notify(9999, notification);
    }
}