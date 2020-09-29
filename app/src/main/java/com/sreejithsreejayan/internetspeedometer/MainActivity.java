package com.sreejithsreejayan.internetspeedometer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 122;
    private static final String CHANNEL_ID = "ID_JUST_CREATED";

    private long lastRX=0;
    private long lastTX=0;
    private long lastTime=0;


    Speed mSpeed=new Speed();
    Canvas canvas;
    Paint speed;
    Paint unit;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
            setupIconGen();

        getRealTimeSpeed();
        createNotificationChannel();
    }

    private void getRealTimeSpeed() {

        lastRX= TrafficStats.getTotalRxBytes();
        lastTX = TrafficStats.getTotalTxBytes();
        lastTime= System.currentTimeMillis();

        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                long currentRX= TrafficStats.getTotalRxBytes();
                long currentTX = TrafficStats.getTotalTxBytes();
                long currentTime = System.currentTimeMillis();

                long uploadBytes= currentRX-lastRX;
                long downloadBytes = currentTX-lastTX;
                long usageTime = currentTime-lastTime;

                lastRX=currentRX;
                lastTX=currentTX;
                lastTime=currentTime;

//                NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
//                NetworkStats.Bucket bucket;
//                try {
//                    bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,"",0,System.currentTimeMillis());
//                    long rxBytes = bucket.getRxBytes();
//                    long txBytes = bucket.getTxBytes();
//                    Log.d("TAG", "run: Test"+rxBytes+txBytes);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                mSpeed.calculateSpeed(uploadBytes,downloadBytes,usageTime);
                Log.d("TAG", "run: background");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startNotification();
                }
            }
        };
        worker.scheduleWithFixedDelay(runnable,1000,1000, TimeUnit.MILLISECONDS);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startNotification() {
        Log.d("TAG", "startNotification: ");
        Notification.Builder notification =new Notification.Builder(this,CHANNEL_ID)
                .setSmallIcon(getIcon())
                .setContentTitle("Upload: "+mSpeed.getSpeed("upSpeed").nSpeed+mSpeed.getSpeed("upSpeed").nSpeedUnit+"  "+"Download: "+ mSpeed.getSpeed("downSpeed").nSpeed+mSpeed.getSpeed("downSpeed").nSpeedUnit)
//                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentText("some text and working on it")
                .setPriority(Notification.PRIORITY_MAX)
                .setOnlyAlertOnce(true)
                .setOngoing(true);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notification.build());
    }

    private void setupIconGen() {
        speed = new Paint();
        speed.setAntiAlias(true);
        speed.setColor(getResources().getColor(R.color.white));
        speed.setTextAlign(Paint.Align.CENTER);
        speed.setTextSize(65);
        speed.setTypeface(Typeface.DEFAULT_BOLD);

        unit = new Paint();
        unit.setAntiAlias(true);
        unit.setColor(getResources().getColor(R.color.white));
        unit.setTextAlign(Paint.Align.CENTER);
        unit.setTextSize(40);
        unit.setTypeface(Typeface.DEFAULT_BOLD);

        image = Bitmap.createBitmap(96, 96, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(image);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public Icon getIcon() {
        image.eraseColor(getResources().getColor(R.color.transparent));
        canvas.drawText(mSpeed.getSpeed("downSpeed").nSpeed, 48, 52, speed);
        canvas.drawText(mSpeed.getSpeed("downSpeed").nSpeedUnit, 48, 95, unit);
        return Icon.createWithBitmap(image);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "default";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}