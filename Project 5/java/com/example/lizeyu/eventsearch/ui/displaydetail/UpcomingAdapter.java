package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.R;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>  {

    private Context mCtx;
    private List<UpcomingEvent> upcomingEventList;
    public UpcomingAdapter(Context mCtx, List<UpcomingEvent> upcomingEventList){
        this.mCtx = mCtx;
        this.upcomingEventList = upcomingEventList;
    }
    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_upcoming, null);
        return new UpcomingAdapter.UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder upcomingViewHolder, int i) {
        final UpcomingEvent cur = upcomingEventList.get(i);
        upcomingViewHolder.name.setText(cur.name);
        upcomingViewHolder.artist.setText(cur.artist);
        upcomingViewHolder.time.setText(cur.time);
        upcomingViewHolder.type.setText(cur.type);
        if(cur.url!=null){
            upcomingViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(cur.url));
                    mCtx.startActivity(browserIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return upcomingEventList.size();
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder{
        TextView name,artist,time,type;
        CardView container;
        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.up_name);
            artist = itemView.findViewById(R.id.up_artist);
            time = itemView.findViewById(R.id.up_time);
            type = itemView.findViewById(R.id.up_type);
            container = itemView.findViewById(R.id.up_event_container);
        }
    }
}
