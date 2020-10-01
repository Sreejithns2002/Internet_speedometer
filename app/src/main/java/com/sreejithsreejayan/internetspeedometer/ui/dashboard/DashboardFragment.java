package com.sreejithsreejayan.internetspeedometer.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sreejithsreejayan.internetspeedometer.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;

    private Button wifi,mob,both;
    private String whichStats = "both";

    private BarChart barChart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        wifi=root.findViewById(R.id.WifiBtn);
        wifi.setOnClickListener(this);
        mob=root.findViewById(R.id.MobBtn);
        mob.setOnClickListener(this);
        both=root.findViewById(R.id.WifiAndMobBtn);
        both.setOnClickListener(this);

        barChart=root.findViewById(R.id.BarChart);
        BarDataSet barDataSet = new BarDataSet(dataValues1(),"dataset1");
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();

        return root;
    }

    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry>dataVals=new ArrayList<>();
        dataVals.add(new BarEntry(0,7));
        dataVals.add(new BarEntry(1,8));
        dataVals.add(new BarEntry(2,6));
        dataVals.add(new BarEntry(3,7));
        dataVals.add(new BarEntry(4,6));
        dataVals.add(new BarEntry(5,8));
        dataVals.add(new BarEntry(6,7));
        dataVals.add(new BarEntry(7,6));
        return dataVals;
    }

    private void restart() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(DashboardFragment.this).attach(DashboardFragment.this).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.WifiBtn:
                restart();
                whichStats="wifi";
                break;
            case R.id.MobBtn:
                whichStats="mobile";
                restart();
                break;
            case R.id.WifiAndMobBtn:
                whichStats="both";
                restart();
                break;
        }

    }
}