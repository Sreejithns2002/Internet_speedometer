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

import com.sreejithsreejayan.internetspeedometer.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;

    private Button wifi,mob,both;
    private String whichStats = "both";


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

        return root;
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