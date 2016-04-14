package com.natallia.vkategory;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.vkategory.adapters.ImageAdapter;
import com.natallia.vkategory.adapters.ImageRecyclerAdapter;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Natallia on 12.04.2016.
 */
public class FragmentDetail extends Fragment {

    private int mPostID;
    private TextView mTvPost;
    private GridLayout mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    public static FragmentDetail createFragment() {
        FragmentDetail fragment = new FragmentDetail();
        return fragment;
    }

    public int getPostID() {
        return mPostID;
    }

    public void setPostID(int mPostID) {
        this.mPostID = mPostID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_detail_fragment, container, false);

        if (savedInstanceState != null) {
            mPostID = savedInstanceState.getInt("postId", 0);
        }

        try {
            Note post = DataManager.getInstance().getPostById(mPostID);
            CardView cardview = (CardView) view.findViewById(R.id.layoutDetailFragment);
           // int width = cardview.getWidth()/2;
            mTvPost = (TextView) view.findViewById(R.id.tvPost);
            mRecyclerView = (GridLayout) view.findViewById(R.id.gvPhoto);
           // mTvPost.setMovementMethod(new ScrollingMovementMethod());
            mTvPost.setText(post.getText());
            List<Photo> photos = DataManager.getInstance().getPhotoList(post);

            mRecyclerView.setColumnCount(2);
            mLayoutManager   = new LinearLayoutManager(getActivity());
//            mRecyclerView.setLayoutManager(mLayoutManager);
//            ImageRecyclerAdapter mAdapter = new ImageRecyclerAdapter(photos,getContext());
//            mRecyclerView.setAdapter(mAdapter);


                //mRecyclerView.setNumColumns(GridView.AUTO_FIT);
               // ImageAdapter mAdapter = new ImageAdapter(getContext(), photos);

           //lButtonParams.




            for (int i = 0; i <photos.size() ; i++) {
                GridLayout.LayoutParams lButtonParams = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(0, 1f));// rams(GridLayout.LayoutParams.WRAP_CONTENT,GridLayout.LayoutParams.WRAP_CONTENT);


                ImageView imageView =  new ImageView(getContext());
                imageView.setLayoutParams(lButtonParams);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setId(i);


               // imageView

                //imageView.setOnClickListener(getEditText);
                Picasso.with(getContext().getApplicationContext()) //передаем контекст приложения
                        .load(photos.get(i).getVkURL_640())
                        .placeholder(android.R.drawable.star_big_on)
                        .into(imageView);
                mRecyclerView.addView(imageView);
            }


           // mRecyclerView.setAdapter(mAdapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("postId", mPostID);
    }


}
