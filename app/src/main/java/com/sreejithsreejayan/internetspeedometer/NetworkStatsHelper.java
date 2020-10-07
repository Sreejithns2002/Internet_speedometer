package com.sreejithsreejayan.internetspeedometer;

import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
@TargetApi(Build.VERSION_CODES.M)
public class NetworkStatsHelper {

//    todo complete the class with the necessery codes

    NetworkStatsManager networkStatsManager;
    int packageUid;

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager, int packageUid) {
        this.networkStatsManager = networkStatsManager;
        this.packageUid = packageUid;
    }

    public long getAllRxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getAllRxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getSllRxBytesBoth(Context context){
        return getAllRxBytesMobile(context)+getAllRxBytesWifi();
    }

    public long getAllTxBytesBoth(Context context){
        return getAllTxBytesMobile(context)+getAllTxBytesWifi();
    }

    public long getPackageRxBytesMobile(Context context) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                0,
                System.currentTimeMillis(),
                packageUid);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public long getPackageTxBytesMobile(Context context) {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                0,
                System.currentTimeMillis(),
                packageUid);

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    public long getPackageRxBytesWifi() {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                0,
                System.currentTimeMillis(),
                packageUid);

        long rxBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            rxBytes += bucket.getRxBytes();
        }
        networkStats.close();
        return rxBytes;
    }

    public long getPackageTxBytesWifi() {
        NetworkStats networkStats = null;
        networkStats = networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_WIFI,
                "",
                0,
                System.currentTimeMillis(),
                packageUid);

        long txBytes = 0L;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            txBytes += bucket.getTxBytes();
        }
        networkStats.close();
        return txBytes;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }

    public ArrayList<BarEntry> getBarEntryArrayList(){
        ArrayList<BarEntry>dataVals=new ArrayList<>();

        Calendar startCalendar,refCalendar,stopCalendar;
        refCalendar=Calendar.getInstance();
        refCalendar.add(Calendar.DAY_OF_MONTH,-5);
        startCalendar= Calendar.getInstance();
        stopCalendar=Calendar.getInstance();

        NetworkStats.Bucket bucket;
        try {
            for (int i=0;i<5;i++){
                startCalendar.set(refCalendar.get(Calendar.YEAR),refCalendar.get(Calendar.MONTH),refCalendar.get(Calendar.DAY_OF_MONTH),0,0);
                stopCalendar.set(refCalendar.get(Calendar.YEAR),refCalendar.get(Calendar.MONTH),refCalendar.get(Calendar.DAY_OF_MONTH)+1,0,0);

                bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,"",startCalendar.getTimeInMillis(),stopCalendar.getTimeInMillis());
                long rxBytes = bucket.getRxBytes();
                long txBytes = bucket.getTxBytes();
                long dataInMb = rxBytes/1000000;
                dataVals.add(new BarEntry(i,dataInMb));
                refCalendar.add(Calendar.DAY_OF_MONTH,1);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return dataVals;
    }




}
