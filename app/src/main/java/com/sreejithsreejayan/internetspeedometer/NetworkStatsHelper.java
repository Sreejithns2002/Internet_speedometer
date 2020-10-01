package com.sreejithsreejayan.internetspeedometer;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class NetworkStatsHelper {
    Calendar startCalendar;
    Calendar stopCalendar;
    Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void run(){
        startCalendar=Calendar.getInstance();
        stopCalendar=Calendar.getInstance();
        startCalendar.add(Calendar.DATE,-1);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) mContext.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(NetworkStats.Bucket.DEFAULT_NETWORK_ALL,"",,System.currentTimeMillis());
            long rxBytes = bucket.getRxBytes();
            long txBytes = bucket.getTxBytes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
