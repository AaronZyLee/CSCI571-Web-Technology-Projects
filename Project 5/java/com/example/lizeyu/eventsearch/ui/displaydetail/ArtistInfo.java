package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.Artist;
import com.example.lizeyu.eventsearch.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ArtistInfo extends Fragment {
    private RecyclerView recyclerView;
    private TextView notFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.artist_info, container, false);
        notFound = rootView.findViewById(R.id.textView7);
        recyclerView = rootView.findViewById(R.id.artist_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        try{
            JSONArray artists = new JSONArray(getArguments().getString("artists"));
            List<Artist> artistList = new ArrayList<>();
            for(int i=0;i<artists.length();i++){
                if(i>=2)    break;
                Artist cur = new Artist();
                JSONObject temp = (JSONObject)artists.get(i);
                if(temp.has("name"))    cur.name = temp.getString("name");
                if(temp.has("pname"))   cur.pname = temp.getString("pname");
                if(temp.has("followers"))   cur.followers = temp.getString("followers");
                if(temp.has("popularity"))  cur.popularity = temp.getString("popularity");
                if(temp.has("spotify")) cur.spotify = temp.getString("spotify");
                if(temp.has("images")){
                    JSONArray images = temp.getJSONArray("images");
                    for(int j=0;j<images.length();j++){
                        JSONObject cur_image = (JSONObject)images.get(j);
                        if(cur_image.has("link"))
                            cur.images.add(cur_image.getString("link"));
                        if(cur.images.size()>=8)    break;
                    }
                }
                artistList.add(cur);
            }
            ArtistAdapter artistAdapter = new ArtistAdapter(this.getActivity(),artistList);
            //ImageAdapter imageAdapter = new ImageAdapter(this.getActivity(),artistList.get(0).images);
            //recyclerView.setAdapter(imageAdapter);
            recyclerView.setAdapter(artistAdapter);
            if(artistList.size() == 0){
                notFound.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }
}
