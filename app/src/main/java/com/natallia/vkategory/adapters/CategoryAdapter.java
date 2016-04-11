package com.natallia.vkategory.adapters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.vkategory.MainActivity;
import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.CategoryFragmentEventHandler;
import com.natallia.vkategory.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    private List<Category> categories;
    private Activity activity;
    private CategoryFragmentEventHandler mCategoryFragmentEventHandler;
    private boolean forChoosing;
    private ViewGroup.LayoutParams lParams1;
    private int oldWidth;

    public CategoryFragmentEventHandler getCategoryFragmentEventHandler() {
        return mCategoryFragmentEventHandler;
    }

    public void setCategoryFragmentEventHandler(CategoryFragmentEventHandler mCategoryFragmentEventHandler) {
        this.mCategoryFragmentEventHandler = mCategoryFragmentEventHandler;
    }

    public CategoryAdapter(List<Category> categories,Activity activity){
        this.categories = categories;
        this.activity = activity;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, final int position) {
        holder.categoryName.setText(categories.get(position).getName());
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO переписать через интерфейс по книжке!!!
                ((MainActivity) activity).refreshPostsFragment(categories.get(holder.getAdapterPosition()).getId());
            }
        });

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 64f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                holder.frameLayout.setCardElevation((Float) animation.getAnimatedValue());
                Log.d("motion_loc", holder.categoryName.getText().toString());
            }
        });

        valueAnimator.setDuration(100);


        holder.frameLayout.getChildAt(0).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("motion", event.toString());
                Log.d("motion", String.valueOf(event.getAction()));

                if (event.getAction() == DragEvent.ACTION_DROP) {


                    Log.d("motion_drop", holder.categoryName.getText().toString());

                            if (mCategoryFragmentEventHandler != null) {
                                int id_post = event.getClipData().getItemAt(0).getIntent().getIntExtra("id_post", 0);
                                mCategoryFragmentEventHandler.OnPostCategoryChange(id_post, categories.get(holder.getAdapterPosition()).getId());
                                valueAnimator.reverse();
                                ((MainActivity)activity).CategoryForChoosing(false);
                            }

                }


                if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION ) {


                    Log.d("motion_location", holder.categoryName.getText().toString());
                    float x = event.getX();
                    float y = event.getY();


                            if (!valueAnimator.isRunning() && valueAnimator.getAnimatedFraction() <= 0.1f){
                                valueAnimator.start();
                            }

                }

                if (event.getAction() == DragEvent.ACTION_DRAG_EXITED ) {
                    Log.d("motion_exited", holder.categoryName.getText().toString());
                    valueAnimator.reverse();
                }

                if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {

                    float x = event.getX();
                    float y = event.getY();

                    Rect r = new Rect();
                    for (int i = 0; i < holder.frameLayout.getChildCount(); i++) {
                        holder.frameLayout.getChildAt(i).getHitRect(r);
                        if (r.contains((int) x, (int) y)) {
                            Log.d("motion_entered", String.valueOf(i));
                        }
                    }
                }

                return true;
            }
        });
        oldWidth = holder.frameLayout.getWidth();
//        if (forChoosing) {
//            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 64f);
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    holder.frameLayout.setCardElevation((Float) animation.getAnimatedValue());
//                    lParams1 =  holder.frameLayout.getLayoutParams();
//
//                    lParams1.width = lParams1.width+20;
//                }
//
//
//            });
//            valueAnimator.setDuration(100);
//            valueAnimator.start();
//
//            //holder.frameLayout.setMinimumWidth(holder.frameLayout.getWidth()+15);
//
//            //holder.frameLayout.setCardElevation(20f);
//        }
//        else {
//            holder.frameLayout.setCardElevation(4f);
//           // lParams1.width = oldWidth;
//        }


    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public void categoryForChoosing(boolean forChoose){
        forChoosing = forChoose;

        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        CardView frameLayout;

        public CategoryHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tvCategory);
            frameLayout = (CardView)itemView.findViewById(R.id.layoutCategory);
            frameLayout.setMaxCardElevation(30f);
            }
        }
    }

