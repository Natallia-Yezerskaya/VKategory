package com.natallia.vkategory.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.OnLoadMoreListener;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Natallia on 23.03.2016.
 */
public class NotesAdapter extends RecyclerView.Adapter{

    private List<Note> notes;
    private Activity activity;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    public NotesAdapter(List<Note> notes, Activity activity,Context context, RecyclerView recyclerView){
        this.notes = notes;
        this.activity = activity;
        this.context = context;


        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem+ visibleThreshold )) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }
    public void removeNote(int postId){

        for (int i = 0; i <notes.size() ; i++) {
            if(notes.get(i).getId()==postId){
                notes.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return notes.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void addNote(Note post){
        notes.add(post);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.post_layout, parent, false);

            vh = new NotesHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }



    private View.DragShadowBuilder getTextThumbnailBuilder(Context context, CharSequence text) {
        TextView shadowView = (TextView) View.inflate(context,
                R.layout.button_layout, null);

        if (shadowView == null) {
            throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
        }

        if (text.length() > 50) {
            text = text.subSequence(0, 50);
        }
        shadowView.setText(text);
        //shadowView.setTextColor(context.getTextColors());

        //shadowView.setTextAppearance(mTextView.getContext(), R.styleable);
        shadowView.setGravity(Gravity.CENTER);

        shadowView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        final int size = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        shadowView.measure(size, size);

        shadowView.layout(0, 0, shadowView.getMeasuredWidth(), shadowView.getMeasuredHeight());
        shadowView.invalidate();
        return new View.DragShadowBuilder(shadowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NotesHolder) {

            NotesHolder notesHolder = (NotesHolder) holder;

            notesHolder.noteText.setText(notes.get(position).getText());

            //if (!(notes.get(position).getPhotos().size() == 0)) {


                Iterator<Photo> iter = notes.get(position).getPhotos().iterator();
                int k = 0;
                List<Photo> photos = new ArrayList<Photo>();
                while (iter.hasNext()) {

                    photos.add(iter.next());
                }

                notesHolder.gridView.setNumColumns(GridView.AUTO_FIT);
                ImageAdapter mAdapter = new ImageAdapter(context, photos, position);
                notesHolder.gridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            //}

            ((NotesHolder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    Log.d("motion", "ONTOUCH");
                    Intent intent = new Intent();
                    intent.putExtra("id_post", notes.get(position).getId());
                    ClipData clipData = ClipData.newIntent("id", intent);
                    View.DragShadowBuilder dragShadowBuilder = getTextThumbnailBuilder(v.getContext(), "HELLO");

                    v.startDrag(clipData, dragShadowBuilder, v, 0);
                    //v.setVisibility(View.GONE);
                    return true;
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder {
        TextView noteText;
        LinearLayout linearLayout;
        ImageView imageView;
        GridView gridView;


        public NotesHolder(View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.tvPost);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.layoutCategory);
            imageView = (ImageView)itemView.findViewById(R.id.ivPhoto);
            gridView = (GridView)itemView.findViewById(R.id.gvMain);

            }
        }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }



    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.loading_progress);
        }
    }
}

