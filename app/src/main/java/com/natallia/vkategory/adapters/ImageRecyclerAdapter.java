package com.natallia.vkategory.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>{

    private List<Photo> mPhotos;
    private Context mContext;
    private PostDraggingListener postDraggingListener;
    private int postID;

    public PostDraggingListener getPostDraggingListener() {
        return postDraggingListener;
    }

    public void setPostDraggingListener(PostDraggingListener postDraggingListener) {
        this.postDraggingListener = postDraggingListener;
    }

    public ImageRecyclerAdapter(List<Photo> photos, Context context, int postID){
        this.mPhotos = photos;
        this.mContext = context;
        this.postID = postID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postDraggingListener != null) {
                    postDraggingListener.onPostSlideShow(postID,position);
                }
            }
        });

        int width =  holder.frameLayout.getWidth();
        Picasso.with(mContext.getApplicationContext()) //передаем контекст приложения
                .load(mPhotos.get(position).getVkURL_640())
                //.transform(new CropMyTransformation())
                .transform(new CropSquareTransformation())
                //.fit()
                //.resize(250,250)
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


    public class CropSquareTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    public class CropMyTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min( Math.min(source.getWidth(), source.getHeight()),300);
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    }

