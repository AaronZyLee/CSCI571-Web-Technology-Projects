package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UpcomingInfo extends Fragment {
    private RecyclerView recyclerView;
    private List<UpcomingEvent> upcomingEventList;
    private List<UpcomingEvent> upcomingEventList_sort;
    private TextView error;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.upcoming_info, container, false);
        recyclerView = rootView.findViewById(R.id.upcoming_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        upcomingEventList = new ArrayList<>();
        error = rootView.findViewById(R.id.textView9);
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.sort_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.sort_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.sort_order);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.sort_order, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(0);



        String upcoming_str = getArguments().getString("upcoming");
        if(upcoming_str.startsWith("[")){
            try {
                JSONArray upcoming_list = new JSONArray(upcoming_str);
                for(int i=0;i<upcoming_list.length();i++){
                    UpcomingEvent cur = new UpcomingEvent();
                    JSONObject up_obj = upcoming_list.getJSONObject(i);
                    cur.name = up_obj.getString("name");
                    cur.type = up_obj.getString("type");
                    cur.artist = up_obj.getString("artist");
                    cur.time = up_obj.getString("time");
                    cur.time_key = up_obj.getString("time_key");
                    cur.url = up_obj.getString("url");
                    upcomingEventList.add(cur);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        upcomingEventList_sort = new ArrayList<>(upcomingEventList);
        if(upcomingEventList.size()!=0)
            error.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cur = parent.getItemAtPosition(position).toString();
                String order = spinner2.getSelectedItem().toString();
                Log.i("eee", order);
                boolean isAscend = order.equals("Ascending");
                if(cur.equals("Default")) {
                    spinner2.setEnabled(false);
                    recyclerView.setAdapter(new UpcomingAdapter(UpcomingInfo.this.getActivity(), upcomingEventList));
                }
                else {
                    spinner2.setEnabled(true);
                    if(cur.equals("Event Name")){
                        if(isAscend)
                            Collections.sort(upcomingEventList_sort,(a,b)->a.name.compareTo(b.name));
                        else
                            Collections.sort(upcomingEventList_sort,(a,b)->b.name.compareTo(a.name));
                    }else if(cur.equals("Time")) {
                        if(isAscend)
                            Collections.sort(upcomingEventList_sort, (a, b) -> a.time_key.compareTo(b.time_key));
                        else
                            Collections.sort(upcomingEventList_sort, (a, b) -> b.time_key.compareTo(a.time_key));
                    }
                    else if(cur.equals("Artist")) {
                        if(isAscend)
                            Collections.sort(upcomingEventList_sort, (a, b) -> a.artist.compareTo(b.artist));
                        else
                            Collections.sort(upcomingEventList_sort, (a, b) -> b.artist.compareTo(a.artist));
                    }
                    else if(cur.equals("Type")){
                        if(isAscend)
                            Collections.sort(upcomingEventList_sort,(a,b)->a.type.compareTo(b.type));
                        else
                            Collections.sort(upcomingEventList_sort,(a,b)->b.type.compareTo(a.type));
                    }

                    recyclerView.setAdapter(new UpcomingAdapter(UpcomingInfo.this.getActivity(), upcomingEventList_sort));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner2.isEnabled()) {
                    String cur = parent.getItemAtPosition(position).toString();
                    String type = spinner.getSelectedItem().toString();
                    Collections.reverse(upcomingEventList_sort);
                    recyclerView.setAdapter(new UpcomingAdapter(UpcomingInfo.this.getActivity(), upcomingEventList_sort));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;
    }
}
