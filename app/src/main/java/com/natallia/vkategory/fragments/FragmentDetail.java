package com.natallia.vkategory.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.List;

public class FragmentDetail extends Fragment {

    private int mPostID;
    private TextView mTvPost;
    private ImageView mImageView;
    private TextView mNoteFrom;
    private TextView mNoteDate;
    private GridLayout mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PostDraggingListener postDraggingListener;

    public static FragmentDetail createFragment() {
        FragmentDetail fragment = new FragmentDetail();
        return fragment;
    }

    public PostDraggingListener getPostDraggingListener() {
        return postDraggingListener;
    }

    public void setPostDraggingListener(PostDraggingListener postDraggingListener) {
        this.postDraggingListener = postDraggingListener;
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
            mTvPost = (TextView) view.findViewById(R.id.tvPost);
            mImageView = (ImageView) view.findViewById(R.id.imageFrom);
            mNoteFrom = (TextView) view.findViewById(R.id.tvFrom);
            mRecyclerView = (GridLayout) view.findViewById(R.id.gvPhoto);
            mTvPost.setText(post.getText());
            mNoteFrom.setText(post.getSourceName());
            Picasso.with(getContext().getApplicationContext())
                    .load(post.getSourcePhoto_50())
                    .placeholder(android.R.drawable.star_big_on)
                    .into(mImageView);
            List<Photo> photos = DataManager.getInstance().getPhotoList(post);

            mLayoutManager   = new LinearLayoutManager(getActivity());
            mRecyclerView.setColumnCount(2);

                for (int i = 0; i <photos.size() ; i++) {
                    GridLayout.LayoutParams lButtonParams = new GridLayout.LayoutParams(GridLayout.spec(i/2), GridLayout.spec(i%2));
                    final ImageView imageView =  new ImageView(getContext());
                    imageView.setLayoutParams(lButtonParams);
                    imageView.setPadding(2, 2, 2, 2);
                    imageView.setId(i);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (postDraggingListener != null) {
                                postDraggingListener.onPostSlideShow(mPostID,imageView.getId());
                            }
                        }
                    });
                    Picasso.with(getContext().getApplicationContext()) //передаем контекст приложения
                            .load(photos.get(i).getVkURL_640())
                            .placeholder(android.R.drawable.star_big_on)
                            .into(imageView);
                    mRecyclerView.addView(imageView);
                    }



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
