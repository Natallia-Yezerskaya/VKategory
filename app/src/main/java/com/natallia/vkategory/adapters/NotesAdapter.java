package com.natallia.vkategory.adapters;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.natallia.vkategory.MainActivity;
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



    private DragShadowBuilder getTextThumbnailBuilder(Context context, CharSequence text) {
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
        return new DragShadowBuilder(shadowView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NotesHolder) {

            final NotesHolder notesHolder = (NotesHolder) holder;
            notesHolder.linearLayout.setVisibility(View.VISIBLE);

            notesHolder.noteText.setText(notes.get(position).getText());
            notesHolder.linearLayout.setMaxCardElevation(64f);
            notesHolder.linearLayout.setCardElevation(4f);

            //((NotesHolder) holder).linearLayout.setShadowPadding(5,5,5,5);

            //if (!(notes.get(position).getPhotos().size() == 0)) {


                Iterator<Photo> iter = notes.get(position).getPhotos().iterator();
                int k = 0;
                List<Photo> photos = new ArrayList<Photo>();
                while (iter.hasNext()) {

                    photos.add(iter.next());
                }

                notesHolder.gridView.setNumColumns(GridView.AUTO_FIT);
                final ImageAdapter mAdapter = new ImageAdapter(context, photos, position);
                notesHolder.gridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            //}

            notesHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View v) {
                    ((MainActivity)activity).CategoryForChoosing(true);
                    //ValueAnimator valueAnimator = ValueAnimator.ofFloat()
                    //ViewPropertyAnimator viewPropertyAnimator = new ViewPropertyAnimator();
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

//                    final View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
//                    //View.DragShadowBuilder dragShadowBuilder = getTextThumbnailBuilder(v.getContext(), "HELLO");
//
//                    v.startDrag(clipData, dragShadowBuilder, v, 0);
//                    //v.setVisibility(View.INVISIBLE);
                    return true;
                }
            });

            notesHolder.linearLayout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    final View dragView=(View) event.getLocalState();
                    if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                        Log.d("motion_ended", event.toString());
                        if(dropEventNotHandled(event)){
                            dragView.post(new Runnable() {
                                @Override
                                public void run() {
                                    dragView.setVisibility(View.VISIBLE);
                                    ((MainActivity)activity).CategoryForChoosing(false);
                                }
                            });
                        }
//                        v.setVisibility(View.VISIBLE);
//                        event.

                    }
                    return false;
                }

                private boolean dropEventNotHandled (DragEvent event){
                    return !event.getResult();
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
        CardView linearLayout;
        GridView gridView;


        public NotesHolder(View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.tvPost);
            linearLayout = (CardView)itemView.findViewById(R.id.layoutPost);
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


}

