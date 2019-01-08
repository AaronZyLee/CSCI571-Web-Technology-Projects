package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lizeyu.eventsearch.Artist;
import com.example.lizeyu.eventsearch.R;


import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private Context mCtx;
    private List<Artist> artistList;

    public ArtistAdapter(Context mCtx, List<Artist> artistList){
        this.mCtx = mCtx;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_artist, null);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder artistViewHolder, int i) {
        final Artist artist = artistList.get(i);
        artistViewHolder.name.setText(artist.name);
        if(artist.pname == null)
            artistViewHolder.pname_w.setVisibility(View.GONE);
        else {
            artistViewHolder.pname.setText(artist.pname);
            artistViewHolder.pname_w.setVisibility(View.VISIBLE);
        }
        if(artist.followers == null)
            artistViewHolder.followers_w.setVisibility(View.GONE);
        else{
            artistViewHolder.followers.setText(artist.followers);
            artistViewHolder.followers_w.setVisibility(View.VISIBLE);}
        if(artist.popularity == null)
            artistViewHolder.popularity_w.setVisibility(View.GONE);
        else{
            artistViewHolder.popularity.setText(artist.popularity);
            artistViewHolder.popularity_w.setVisibility(View.VISIBLE);}
        if(artist.spotify == null)
            artistViewHolder.spotify_w.setVisibility(View.GONE);
        else{
            artistViewHolder.spotify.setClickable(true);
            artistViewHolder.spotify.setMovementMethod(LinkMovementMethod.getInstance());
            artistViewHolder.spotify.setText(Html.fromHtml("<a href='" + artist.spotify + "'>Spotify</a>"));
            artistViewHolder.spotify_w.setVisibility(View.VISIBLE);
        }
        artistViewHolder.images.setHasFixedSize(true);
        artistViewHolder.images.setLayoutManager(new LinearLayoutManager(mCtx));
        ImageAdapter imageAdapter = new ImageAdapter(mCtx,artist.images);
        artistViewHolder.images.setAdapter(imageAdapter);

    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder{
        TextView name,pname,followers,popularity,spotify;
        TableRow pname_w, followers_w, popularity_w, spotify_w;
        RecyclerView images;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.artist_name);
            pname = itemView.findViewById(R.id.artist_info_row1_input);
            followers= itemView.findViewById(R.id.artist_info_row2_input);
            popularity = itemView.findViewById(R.id.artist_info_row3_input);
            spotify = itemView.findViewById(R.id.artist_info_row4_input);
            pname_w = itemView.findViewById(R.id.artist_info_row1);
            followers_w = itemView.findViewById(R.id.artist_info_row2);
            popularity_w = itemView.findViewById(R.id.artist_info_row3);
            spotify_w = itemView.findViewById(R.id.artist_info_row4);
            images = itemView.findViewById(R.id.images);
        }
    }
}
