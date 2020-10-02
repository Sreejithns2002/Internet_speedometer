package com.sreejithsreejayan.internetspeedometer.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sreejithsreejayan.internetspeedometer.MainActivity2;
import com.sreejithsreejayan.internetspeedometer.NetworkStatsHelper;
import com.sreejithsreejayan.internetspeedometer.R;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        button=root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
//                Intent intent= new Intent(getContext(),MainActivity2.class);
//                startActivity(intent);

                String TAG = "test";
                Calendar startCalendar;
                Calendar stopCalendar;

                startCalendar= Calendar.getInstance();
                stopCalendar=Calendar.getInstance();
                startCalendar.set(2020,10,2,0,0);
                stopCalendar.set(2020,10,3,0,0);
                NetworkStatsManager networkStatsManager = (NetworkStatsManager) getContext().getSystemService(Context.NETWORK_STATS_SERVICE);
                NetworkStats.Bucket bucket;
                try {
                    bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,"",0,System.currentTimeMillis());
                    long rxBytes = bucket.getRxBytes();
                    long txBytes = bucket.getTxBytes();
                    Log.d(TAG, "run: "+startCalendar.getTimeInMillis()+" "+stopCalendar.getTimeInMillis()+" "+System.currentTimeMillis()+"  "+rxBytes+"  "+txBytes);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }
}