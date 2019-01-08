package com.example.lizeyu.eventsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FavoriteList extends Fragment {
    RecyclerView recyclerView;
    List<Event> eventList;
    private TextView notFound;
    private LinearLayout progressBar;
    private SharedPreferences sp;
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_display_list, container, false);
        getFavorite();
        return rootView;
    }

    public void getFavorite(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.search_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        progressBar = rootView.findViewById(R.id.progressbar_wrapper);
        progressBar.setVisibility(View.GONE);
        eventList = new ArrayList<>();
        notFound = rootView.findViewById(R.id.textView3);
        notFound.setText("No Favourites");
        notFound.setVisibility(View.VISIBLE);
        sp = this.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sp.getAll().size() != 0){
            for(String key: sp.getAll().keySet()){
                try {
                    JSONObject values = new JSONObject(sp.getString(key, null));
                    String name = values.getString("name");
                    String venueName = values.getString("venueName");
                    String time = values.getString("time");
                    String category = values.getString("category");
                    eventList.add(new Event(key, name, time, venueName, category));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            notFound.setVisibility(View.GONE);
        }
        EventsAdapter2 adapter = new EventsAdapter2(this.getActivity(), eventList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        getFavorite();


    }


//    public void getDetails(View view){
//        String id = (String)view.getTag(R.id.tag0);
//        for(int i=0;i<eventList.size();i++){
//            if(eventList.get(i).id.equals(id)){
//                eventList.remove(i);
//                break;
//            }
//        }
//        if(eventList.size() == 0){
//            notFound.setVisibility(View.VISIBLE);
//        }else{
//            EventsAdapter adapter = new EventsAdapter(this.getActivity(), eventList);
//            recyclerView.setAdapter(adapter);
//        }
//    }
}
