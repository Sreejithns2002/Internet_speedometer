package com.sreejithsreejayan.internetspeedometer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.ViewHolder> {

    private ArrayList<DataUsageItem> mItem;

    public UsageAdapter(ArrayList<DataUsageItem> mItem) {
        this.mItem = mItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context= parent.getContext();
        LayoutInflater layoutInflater= LayoutInflater.from(context);

        View view=layoutInflater.inflate(R.layout.app_usage_data_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DataUsageItem dataUsageItem = mItem.get(position);

//        TODO:set text codes

    }

    @Override
    public int getItemCount() {

        return mItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView appicon;
        TextView appName,dataUsage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appicon=itemView.findViewById(R.id.iconView);
            appName=itemView.findViewById(R.id.pakageNameView);
            dataUsage=itemView.findViewById(R.id.pakageUsageView);
        }
    }
}
