package com.example.lizeyu.eventsearch.ui.displaydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.lizeyu.eventsearch.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mCtx;
    private List<String> imageList;

    public ImageAdapter(Context mCtx, List<String> imageList){
        this.mCtx = mCtx;
        this.imageList = imageList;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_artist_image, null);
        return new ImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        String url = imageList.get(i);
        Log.i("url:", url);
        Glide.with(mCtx)
                .load(url)
                .into(imageViewHolder.imageView);
//        imageViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.art_icon));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.artist_image);
        }
    }

}
