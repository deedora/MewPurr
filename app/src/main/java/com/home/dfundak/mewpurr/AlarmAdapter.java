package com.home.dfundak.mewpurr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DoraF on 25/02/2018.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<Alarm> mAlarms;

    public AlarmAdapter(ArrayList<Alarm> alarms) {
        mAlarms = alarms;
    }

    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View alarmView = inflater.inflate(R.layout.time_list_item, parent, false);
        ViewHolder alarmViewHolder = new ViewHolder(alarmView);
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.ViewHolder holder, int position) {
        Alarm alarm = this.mAlarms.get(position);
        holder.time.setText(alarm.getTime());
    }

    @Override
    public int getItemCount() {
        return this.mAlarms.size();
    }

    public void insert(Alarm alarm) {
        this.mAlarms.add(alarm);
        this.notifyDataSetChanged();
    }
    public void deleteAt(int position) {
        this.mAlarms.remove(position);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.remove = (ImageView) itemView.findViewById(R.id.remove_button);
        }
    }
}
