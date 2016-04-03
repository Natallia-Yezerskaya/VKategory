package com.natallia.vkategory.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.widget.GridView.*;

/**
 * Created by Natallia on 28.03.2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Photo> mPhotos;
    private int mPosition;

    public ImageAdapter(Context mContext, List<Photo> photos,int mPosition) {
        this.mContext = mContext;
        this.mPhotos = photos;
        this.mPosition = mPosition;
    }


    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
       // if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
           // imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT,GridView.LayoutParams.WRAP_CONTENT));
           // imageView.setLayoutParams(new GridView.LayoutParams(85,85));
           // imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //imageView.setPadding(2, 2, 2, 2);

      //  } else {
            //imageView = (ImageView) convertView;
      //  }


        Picasso.with(mContext.getApplicationContext()) //передаем контекст приложения
                .load(mPhotos.get(position).getVkURL_640())
                .placeholder(android.R.drawable.star_big_on)
                .into(imageView);


        return imageView;
    }
}
