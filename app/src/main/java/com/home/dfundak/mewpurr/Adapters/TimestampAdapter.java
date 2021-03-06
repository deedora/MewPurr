package com.home.dfundak.mewpurr.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.dfundak.mewpurr.Class.Timestamp;
import com.home.dfundak.mewpurr.R;

import java.util.ArrayList;

public class TimestampAdapter extends RecyclerView.Adapter<TimestampAdapter.ViewHolder> {

    private ArrayList<Timestamp> mTimestamps;

    public TimestampAdapter(ArrayList<Timestamp> timestamps) {
        mTimestamps = timestamps;
    }

    @Override
    public TimestampAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View timestampView = inflater.inflate(R.layout.timestamp_list_item, parent, false);
        TimestampAdapter.ViewHolder timestampViewHolder = new TimestampAdapter.ViewHolder(timestampView);
        return timestampViewHolder;
    }

    @Override
    public void onBindViewHolder(TimestampAdapter.ViewHolder holder, int position) {
        Timestamp timestamp = this.mTimestamps.get(position);
        holder.time.setText(timestamp.getDate().substring(0, 16));
    }

    @Override
    public int getItemCount() {
        return this.mTimestamps.size();
    }

    public void updateData(ArrayList<Timestamp> viewModels) {
        mTimestamps.clear();
        mTimestamps.addAll(viewModels);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            this.time = itemView.findViewById(R.id.time);
        }
    }
}
