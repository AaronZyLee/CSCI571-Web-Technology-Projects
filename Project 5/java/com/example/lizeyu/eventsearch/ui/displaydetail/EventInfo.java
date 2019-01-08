package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.R;

public class EventInfo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_info, container, false);
        String team = getArguments().getString("team");
        String venue_name = getArguments().getString("venue_name");
        String time = getArguments().getString("time");
        String status = getArguments().getString("status");
        String url = getArguments().getString("url");
        String seatmap = getArguments().getString("seatmap");
        String category = getArguments().getString("category");
        String priceRange = getArguments().getString("priceRanges");
        if(team!=null){
            ((TextView)rootView.findViewById(R.id.event_info_row1_input)).setText(team);
        }else{
            rootView.findViewById(R.id.event_info_row1).setVisibility(View.GONE);
        }
        if(venue_name!=null){
            ((TextView)rootView.findViewById(R.id.event_info_row2_input)).setText(venue_name);
        }else{
            rootView.findViewById(R.id.event_info_row2).setVisibility(View.GONE);
        }
        if(time!=null){
            ((TextView)rootView.findViewById(R.id.event_info_row3_input)).setText(time);
        }else{
            rootView.findViewById(R.id.event_info_row3).setVisibility(View.GONE);
        }
        if(category!=null){
            ((TextView)rootView.findViewById(R.id.event_info_row4_input)).setText(category);
        }else{
            rootView.findViewById(R.id.event_info_row4).setVisibility(View.GONE);
        }
        if(status!=null){
            ((TextView)rootView.findViewById(R.id.event_info_row5_input)).setText(status);
        }else{
            rootView.findViewById(R.id.event_info_row5).setVisibility(View.GONE);
        }
        if(url != null){
            TextView ticket_url = rootView.findViewById(R.id.event_info_row6_input);
            ticket_url.setClickable(true);
            ticket_url.setMovementMethod(LinkMovementMethod.getInstance());
            ticket_url.setText(Html.fromHtml("<a href='" + url + "'>Ticketmaster</a>"));
        }else{
            rootView.findViewById(R.id.event_info_row6).setVisibility(View.GONE);
        }
        if(seatmap != null){
            TextView seatmap_url = rootView.findViewById(R.id.event_info_row7_input);
            seatmap_url.setClickable(true);
            seatmap_url.setMovementMethod(LinkMovementMethod.getInstance());
            seatmap_url.setText(Html.fromHtml("<a href='" + seatmap + "'>View Here</a>"));
        }else{
            rootView.findViewById(R.id.event_info_row7).setVisibility(View.GONE);
        }
        if(priceRange!=null){
            TextView price = rootView.findViewById(R.id.event_info_row8_input);
            price.setText(priceRange);
        }else{
            rootView.findViewById(R.id.event_info_row8).setVisibility(View.GONE);
        }

//        textView.setClickable(true);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        String text = "<a href='http://www.google.com'> Google </a>";
//        textView.setText(Html.fromHtml(text));

        return rootView;
    }
}
