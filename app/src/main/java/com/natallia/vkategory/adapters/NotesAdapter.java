package com.natallia.vkategory.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.natallia.vkategory.MainActivity;
import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.OnLoadMoreListener;
import com.natallia.vkategory.models.Note;
import com.natallia.vkategory.models.Photo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NotesHolder) {

            final NotesHolder notesHolder = (NotesHolder) holder;
            notesHolder.cardView.setVisibility(View.VISIBLE);

            notesHolder.noteText.setText(notes.get(position).getText());
            notesHolder.cardView.setMaxCardElevation(20f);
            notesHolder.cardView.setCardElevation(4f);

                Iterator<Photo> iter = notes.get(position).getPhotos().iterator();
                int k = 0;
                List<Photo> photos = new ArrayList<Photo>();
                while (iter.hasNext()) {
                    photos.add(iter.next());
                }


                LinearLayoutManager mLayoutManager   = new LinearLayoutManager(context);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                Resources r = context.getResources();
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, r.getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                params.height = height * photos.size();
                notesHolder.gridView.setLayoutParams(params);
                notesHolder.gridView.setLayoutManager(mLayoutManager);
                ImageRecyclerAdapter mAdapter = new ImageRecyclerAdapter(photos,context);
                notesHolder.gridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//                notesHolder.cardView.getViewTreeObserver().
//                        addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(notesHolder.cardView));

//                notesHolder.gridView.setNumColumns(GridView.AUTO_FIT);
//                final ImageAdapter mAdapter = new ImageAdapter(context, photos, position);
//                notesHolder.gridView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();

            //}

            notesHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View v) {
                    ((MainActivity) activity).CategoryForChoosing(true);
                    Log.d("motion", "ONTOUCH");
                    Intent intent = new Intent();
                    intent.putExtra("id_post", notes.get(notesHolder.getAdapterPosition()).getId());
                    final ClipData clipData = ClipData.newIntent("id", intent);


                    final CardView cardView = (CardView) v;
                    //cardView.setCardElevation(16f);
                    //ObjectAnimator objectAnimator = ObjectAnimator.ofArgb(cardView, "background", Color.BLACK);
                    //objectAnimator.start();
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 64f);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            cardView.setCardElevation((Float) animation.getAnimatedValue());
                        }


                    });
                    valueAnimator.setDuration(100);
                    valueAnimator.start();
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            final MyDragShadowBuilder dragShadowBuilder = new MyDragShadowBuilder(v);
                            //View.DragShadowBuilder dragShadowBuilder = getTextThumbnailBuilder(v.getContext(), "HELLO");
                            //dragShadowBuilder
                            //dragShadowBuilder.onProvideShadowMetrics(new Point(), new Point(0,0));
                            v.startDrag(clipData, dragShadowBuilder, v, 0);
                            ((CardView) v).setCardElevation(4f);
                            v.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

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
        CardView cardView;
        RecyclerView gridView;


        public NotesHolder(View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.tvPost);
            cardView = (CardView)itemView.findViewById(R.id.layoutPost);
            gridView = (RecyclerView)itemView.findViewById(R.id.gvMain);
//            cardView.getViewTreeObserver().
//                    addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(cardView));
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




    private class MyDragShadowBuilder extends View.DragShadowBuilder{
        public MyDragShadowBuilder(View view) {
            super(view);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
           // super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
            int width, height;

            width = getView().getWidth();
            height = getView().getHeight();

            shadowSize.set(width, height);
            shadowTouchPoint.set(width/5, height/5);
        }
    }

    private class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static int maxHeight = 300;
        private View view;

        public OnViewGlobalLayoutListener(View view) {
            this.view = view;
        }

        @Override
        public void onGlobalLayout() {
            Log.d("height",String.valueOf(view.getHeight()));
            if (view.getHeight() > maxHeight){
                view.getLayoutParams().height = maxHeight;
                view.setLayoutParams(view.getLayoutParams());
            }
        }
    }

}

