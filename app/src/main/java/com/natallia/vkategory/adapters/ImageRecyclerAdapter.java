package com.natallia.vkategory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.natallia.vkategory.R;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>{

    private List<Photo> mPhotos;
    private Context mContext;

    public ImageRecyclerAdapter(List<Photo> photos,  Context context){
        this.mPhotos = photos;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(mContext.getApplicationContext()) //передаем контекст приложения
                .load(mPhotos.get(position).getVkURL_640())
                .placeholder(android.R.drawable.star_big_on)
                .into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = (FrameLayout)itemView.findViewById(R.id.frame);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            }
        }

    }

