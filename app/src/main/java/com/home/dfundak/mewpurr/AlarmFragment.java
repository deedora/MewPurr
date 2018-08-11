package com.home.dfundak.mewpurr;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.dfundak.mewpurr.Class.Alarm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DoraF on 22/02/2018.
 */

public class AlarmFragment extends Fragment {
    Button setTime;
    RecyclerView alarmsLV;
    ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    AlarmAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.alarm_fragment, container, false);
        initializeUI(layout);
        return layout;
    }

    private void initializeUI(View layout) {
        //Load data when app opened
        new GetData().execute(SupportData.getAddressAPI());

        this.setTime = (Button) layout.findViewById(R.id.set_time);
        this.alarmsLV = (RecyclerView) layout.findViewById(R.id.alarms_list_view);

        adapter = new AlarmAdapter(alarms); // Create adapter
        alarmsLV.setAdapter(adapter); //set empty adapter
        alarmsLV.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (timePicker.isShown()) {
                            setAlarm(selectedHour, selectedMinute);
                        }
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        alarmsLV.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final int mpos = position;
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure you want to delete " + alarms.get(position).getTime() + " alarm?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteData(alarms.get(mpos)).execute(SupportData.getAddressSingle(alarms.get(mpos)));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                      }
        }));

        ImageView alarmImage = getActivity().findViewById(R.id.alarm_image);
        alarmImage.setAlpha(0.5f);
        ImageView statsImage = getActivity().findViewById(R.id.stats_image);
        statsImage.setAlpha(1f);
        ImageView homeImage = getActivity().findViewById(R.id.home_image);
        homeImage.setAlpha(1f);
    }

    private void setAlarm(int selectedHour, int selectedMinute) {
        StringBuilder timeString = new StringBuilder();
        if(selectedHour < 10){
            timeString.append("0");
        }
        timeString.append(selectedHour);
        timeString.append(":");
        if (selectedMinute < 10) {
            timeString.append("0");
        }
        timeString.append(selectedMinute);
        new PostData(timeString.toString()).execute(SupportData.getAddressAPI());
    }

    class PostData extends AsyncTask<String, String, String> {
        String alarmName;

        public PostData(String alarmName) {
            this.alarmName = alarmName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            String json = "{\"time\":\"" + alarmName + "\"}";
            hh.PostHTTPData(urlString, json);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new GetData().execute(SupportData.getAddressAPI());
        }
    }

    class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            //Running process
            String stream = null;
            String urlString = params[0];

            HTTPDataHandler http = new HTTPDataHandler();
            stream = http.GetHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Done process

            //GSon to parse Json to Class
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Alarm>>() {
            }.getType();
            alarmsLV.setLayoutManager(new LinearLayoutManager(getActivity()));
            alarms = gson.fromJson(s, listType); // parse to List

            for (int i = alarms.size(); i < 0; i++) {
                Log.d("alarm", alarms.get(i).getTime());
            }

            adapter.updateData(alarms);
        }
    }

    class DeleteData extends AsyncTask<String, String, String> {
        Alarm alarm;

        public DeleteData(Alarm alarm) {
            this.alarm = alarm;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            String json = "{\"alarm\":\"" + alarm.getTime() + "\"}";
            hh.DeleteHTTPData(urlString, json);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new AlarmFragment.GetData().execute(SupportData.getAddressAPI());
        }
    }
}
