package com.example.lizeyu.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;


public class EventsAdapter2 extends RecyclerView.Adapter<EventsAdapter2.EventsViewHolder> {

    private Context mCtx;
    private List<Event> eventList;
    private AdapterView.OnItemClickListener mItemClickListener;
    private SharedPreferences sp;

    public EventsAdapter2(Context mCtx, List<Event> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
        this.sp = mCtx.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_events, null);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int i) {
        final Event event = eventList.get(i);

        //binding the data with the viewholder views
        holder.name.setText(event.name);
        holder.venue.setText(event.venueName);
        holder.time.setText(event.time);
        holder.category_icon.setImageDrawable(mCtx.getResources().getDrawable(event.icon));
        // Glide.with(mCtx).load("https://media.glamour.com/photos/59566919f4609a21dfa111fa/master/pass/lady-gaga-fashion-awards-getty-1.jpg").into(holder.category_icon);
        holder.ll.setTag(event.id);
        holder.favorite_icon.setTag(R.id.tag0,event.id);
        holder.favorite_icon.setTag(R.id.tag1,event.name);
        holder.favorite_icon.setTag(R.id.tag2,event.venueName);
        holder.favorite_icon.setTag(R.id.tag3,event.time);
        holder.favorite_icon.setTag(R.id.tag4,event.category);

        holder.ll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx,DisplayDetail.class);
                try{
                    JSONObject values = new JSONObject();
                    values.put("name",event.name);
                    values.put("venueName", event.venueName);
                    values.put("category",event.category);
                    values.put("time",event.time);
                    intent.putExtra("id",event.id);
                    intent.putExtra("event", values.toString());
                    intent.putExtra("name",event.name);
                    startActivity(mCtx,intent, new Bundle());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        holder.favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, event.name + " was removed from favorites", Toast.LENGTH_SHORT).show();
                removeAt(i);
            }
        });
        if(mCtx.getSharedPreferences("user",Context.MODE_PRIVATE).contains(event.id))
            holder.favorite_icon.setChecked(true);
        // holder.favorite_icon.setImageDrawable(mCtx.getResources().getDrawable(event.favorite_icon));

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    public void removeAt(int position) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(eventList.get(position).id);
        editor.commit();
        eventList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, eventList.size());

    }

    class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView name, venue, time;
        ImageView category_icon;
        ToggleButton favorite_icon;
        LinearLayout ll;
        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_name);
            venue = itemView.findViewById(R.id.venue_name);
            time = itemView.findViewById(R.id.date_info);
            category_icon = itemView.findViewById(R.id.imageView);
            ll = itemView.findViewById(R.id.event_wrapper);
            favorite_icon = itemView.findViewById(R.id.favorite_icon);
        }

//        @Override
//        public void onClick(View v) {
//            //Log.d("View: ", v.toString());
//            //Toast.makeText(v.getContext(), mTextViewTitle.getText() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            if(v.equals(favorite_icon)){
//                removeAt(getAdapterPosition());
//            }else if (mItemClickListener != null) {
//                mItemClickListener.onItemClick(v, getAdapterPosition());
//            }
//        }
    }
}
