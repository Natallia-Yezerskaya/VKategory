package com.natallia.vkategory.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
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
import com.natallia.vkategory.UI.MyColor;
import com.natallia.vkategory.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    private List<Category> categories;
    private Activity activity;
    private CategoryFragmentEventHandler mCategoryFragmentEventHandler;
    private boolean forChoosing;
    private ViewGroup.LayoutParams lParams1;
    private int oldWidth;
    private int idSelectedCategory ;

    public int getIdSelectedCategory() {
        return idSelectedCategory;
    }

    public void setIdSelectedCategory(int idSelectedCategory) {
        this.idSelectedCategory = idSelectedCategory;
    }

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
        if (position == idSelectedCategory){
            holder.frameLayout.setBackgroundResource(R.drawable.rectangle_rounded_accent);
        }
        else {
            holder.frameLayout.setBackgroundResource(R.drawable.rectangle_rounded);
        }

        holder.frameLayout.getBackground().setColorFilter(MyColor.myColors.get(position).getColor(), PorterDuff.Mode.MULTIPLY);
        holder.frameLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO переписать через интерфейс по книжке!!!
                ((MainActivity) activity).refreshPostsFragment(categories.get(holder.getAdapterPosition()).getId());
                notifyItemChanged(idSelectedCategory);
                idSelectedCategory = holder.getAdapterPosition();



                holder.frameLayout.setBackgroundResource(R.drawable.rectangle_rounded_accent);
                holder.frameLayout.getBackground().setColorFilter(MyColor.myColors.get(holder.getAdapterPosition()).getColor(), PorterDuff.Mode.MULTIPLY);
            }
        });

        //holder.frameLayout.getBackground().setColorFilter(activity.getResources().getColor(R.color.colortemp1), PorterDuff.Mode.MULTIPLY);
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color1 = MyColor.myColors.get(position).getColor();
                int color2 = activity.getResources().getColor(R.color.colortemp2);
                int res = (int) new ArgbEvaluator().evaluate((float)animation.getAnimatedValue(),color1,color2);
                holder.frameLayout.getBackground().setColorFilter(res, PorterDuff.Mode.MULTIPLY);
                holder.frameLayout.setBackgroundResource(R.drawable.rectangle_rounded);
                Log.d("motion_loc", animation.getAnimatedValue().toString());
            }

        });

        valueAnimator.setDuration(300);

//        final ValueAnimator valueAnimator = ValueAnimator.ofArgb(R.color.colortemp1, R.color.colortemp2);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                holder.frameLayout.setCardElevation((Float) animation.getAnimatedValue());
//                Log.d("motion_loc", holder.categoryName.getText().toString());
//            }
//        });
//
//        valueAnimator.setDuration(100);


        holder.frameLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("motion", event.toString());
                Log.d("motion", String.valueOf(event.getAction()));

                if (event.getAction() == DragEvent.ACTION_DROP) {


                    Log.d("motion_drop", holder.categoryName.getText().toString());

                            if (mCategoryFragmentEventHandler != null) {
                                int id_post = event.getClipData().getItemAt(0).getIntent().getIntExtra("id_post", 0);

                                boolean replaced = mCategoryFragmentEventHandler.OnPostCategoryChange(id_post, categories.get(holder.getAdapterPosition()).getId());
                                ((MainActivity) activity).CategoryForChoosing(false);

                                if (replaced) {
                                    valueAnimator.reverse();
                                } else {
                                    return false;
                                }

                            }

                }


                if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION ) {


                    Log.d("motion_location", holder.categoryName.getText().toString());

                            if (!valueAnimator.isRunning() && valueAnimator.getAnimatedFraction() <= 0.1f){
                                valueAnimator.start();
                            }

                       // holder.frameLayout.setBackgroundResource(R.color.colortemp2);
                }

                if (event.getAction() == DragEvent.ACTION_DRAG_EXITED ) {
                    Log.d("motion_exited", holder.categoryName.getText().toString());
                    valueAnimator.reverse();
                    //holder.frameLayout.setBackgroundResource(R.color.colortemp1);
                }

                if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {

                    if (!valueAnimator.isRunning() && valueAnimator.getAnimatedFraction() <= 0.1f){
                        valueAnimator.start();
                    }
                }

                return true;
            }
        });
        oldWidth = holder.frameLayout.getWidth();
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
        FrameLayout frameLayout;

        public CategoryHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.tvCategory);
            frameLayout = (FrameLayout)itemView.findViewById(R.id.layoutCategory);
            //frameLayout.setMaxCardElevation(30f);
            }
        }

    }

