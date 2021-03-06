package com.natallia.vkategory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.AsyncRequestListener;
import com.natallia.vkategory.UI.OnLoadMoreListener;
import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.adapters.NotesAdapter;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Note;

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

    static final String TAG = "POST_FRAGMENT";

    private View footer;

    protected Handler handler;


    private PostDraggingListener postDraggingListener;

    public PostDraggingListener getPostDraggingListener() {
        return postDraggingListener;
    }

    public void setPostDraggingListener(PostDraggingListener postDraggingListener) {
        this.postDraggingListener = postDraggingListener;
    }

    public static PostsFragment createFragment(Intent intent) {

        PostsFragment fragment = new PostsFragment();
        fragment.idCategory = intent.getIntExtra("idCategory",0);

        return fragment; //TODO можно сделать через setID
    }

    public NotesAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(NotesAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
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
        mAdapter.setPostDraggingListener(postDraggingListener);
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

        mRecyclerView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final View dragView = (View) event.getLocalState();
                Log.d("motion_ended_fragment", event.toString());
                if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                    Log.d("motion_ended_fragment", event.toString());
                    if (dropEventNotHandled(event)) {
                        dragView.post(new Runnable() {
                            @Override
                            public void run() {
                                dragView.setVisibility(View.VISIBLE);
                                if (postDraggingListener != null) {
                                    postDraggingListener.onPostDrag(false);
                                }
                            }
                        });
                    }
                } else if (event.getAction() == DragEvent.ACTION_DROP) {
                    return false;
                }

                return true;
            }

            private boolean dropEventNotHandled(DragEvent event) {
                return !event.getResult();
            }
        });

        mAdapter.notifyDataSetChanged();

        Log.d(TAG, "onCreateView: ");

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            idCategory = savedInstanceState.getInt("idCategory",0);
        }
    }

    public void setFocusOnFirstItem(){
        mRecyclerView.smoothScrollToPosition(0);
    }
}
