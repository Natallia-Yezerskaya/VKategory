package com.natallia.vkategory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.Not;
import com.natallia.vkategory.UI.AsyncRequestListener;
import com.natallia.vkategory.UI.OnLoadMoreListener;
import com.natallia.vkategory.adapters.NotesAdapter;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Note;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Natallia on 22.03.2016.
 */
public class PostsFragment extends Fragment {

    private int idCategory;

    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean loading;
    private List<Note> values;

    private View footer;

    protected Handler handler;


    public static PostsFragment createFragment(Intent intent) {

        PostsFragment fragment = new PostsFragment();
        fragment.idCategory = intent.getIntExtra("idCategory",0);

        return fragment;
    }

    public NotesAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(NotesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        handler = new Handler();

        if (savedInstanceState != null){
            idCategory = savedInstanceState.getInt("idCategory",0);
        }

        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPosts);
        mLayoutManager   = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        values = DataManager.getInstance().getPostsListByCategory(idCategory);
        mAdapter = new NotesAdapter(values,getActivity(),getContext(),mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                values.add(null);
                mAdapter.notifyItemInserted(values.size() - 1);

                int start = values.size();
                int end = start + 10;
                DataManager.getInstance().loadPostsList(start, new AsyncRequestListener() {
                    @Override
                    public void onDataReceived(List<Note> notes) {
                        super.onDataReceived(notes);

                        values.remove(values.size() - 1);
                        mAdapter.notifyItemRemoved(values.size());
                        for (int i = 0; i < notes.size(); i++) {
                            values.add(notes.get(i));
                            mAdapter.notifyItemInserted(values.size());
                        }

                        mAdapter.setLoaded();
                    }
                });

            }
        });


        mAdapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void removePostFromView(int postId){
        mAdapter.removeNote(postId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idCategory", idCategory);
    }

}
