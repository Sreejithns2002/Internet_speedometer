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
import java.util.GregorianCalendar;

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

                String TAG = "TAG";
                Calendar startCalendar,refCalendar,stopCalendar;

                refCalendar=Calendar.getInstance();
                refCalendar.add(Calendar.DAY_OF_MONTH,-5);
                startCalendar= Calendar.getInstance();
                stopCalendar=Calendar.getInstance();

                NetworkStatsManager networkStatsManager = (NetworkStatsManager) getContext().getSystemService(Context.NETWORK_STATS_SERVICE);
                NetworkStats.Bucket bucket;
                Log.d(TAG, "onClick: is run");
                try {
                    for (int i=0;i<5;i++){
                        startCalendar.set(refCalendar.get(Calendar.YEAR),refCalendar.get(Calendar.MONTH),refCalendar.get(Calendar.DAY_OF_MONTH),0,0);
                        stopCalendar.set(refCalendar.get(Calendar.YEAR),refCalendar.get(Calendar.MONTH),refCalendar.get(Calendar.DAY_OF_MONTH)+1,0,0);

                        bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,"",startCalendar.getTimeInMillis(),stopCalendar.getTimeInMillis());
                        long rxBytes = bucket.getRxBytes();
                        long txBytes = bucket.getTxBytes();
                        long dataInMb = rxBytes/1000000;
                        Log.d(TAG, "run: start time: "+startCalendar.getTime()+" stop time: "+stopCalendar.getTime()+"  Data in mb: "+dataInMb+"  "+rxBytes+"  "+txBytes);
                        refCalendar.add(Calendar.DAY_OF_MONTH,1);
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }
}