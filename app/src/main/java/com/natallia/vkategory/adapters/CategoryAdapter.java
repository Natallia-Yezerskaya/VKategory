package com.natallia.vkategory.adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.natallia.vkategory.MainActivity;
import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.CategoryFragmentEventHandler;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Category;

import java.util.List;

/**
 * Created by Natallia on 23.03.2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    private List<Category> categories;
    private Activity activity;
    private CategoryFragmentEventHandler mCategoryFragmentEventHandler;

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
                ((MainActivity) activity).refreshPostsFragment(categories.get(holder.getAdapterPosition()).getId());
            }
        });
        holder.frameLayout.getChildAt(0).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("motion", event.toString());
                Log.d("motion", String.valueOf(event.getAction()));

                if (event.getAction() == DragEvent.ACTION_DROP) {


                    Log.d("motion_drop", holder.categoryName.getText().toString());
                    float x = event.getX();
                    float y = event.getY();

                    Rect r = new Rect();
                    for (int i = 0; i < holder.frameLayout.getChildCount(); i++) {
                        holder.frameLayout.getChildAt(i).getHitRect(r);
                        //holder.frameLayout.getChildAt(i).getGlobalV)
                        if (r.contains((int) x, (int) y)) {
                            Log.d("motion_drop", String.valueOf(i));

                            if (mCategoryFragmentEventHandler!=null) {
                                mCategoryFragmentEventHandler.OnPostCategoryChange(event.getClipData().getItemAt(0).getIntent().getIntExtra("id_post", 0),categories.get(position).getId());
                            }

                            //DataManager.getInstance().replacePostsIntoCategory(event.getClipData().getItemAt(0).getIntent().getIntExtra("id_post", 0),categories.get(position).getId());
                            //event.getClipData().getItemAt(0).getIntent().getIntExtra("id_post", 0)

                        }
                    }
                }
                if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED) {

                    float x = event.getX();
                    float y = event.getY();

                    Rect r = new Rect();
                    for (int i = 0; i < holder.frameLayout.getChildCount(); i++) {
                        holder.frameLayout.getChildAt(i).getHitRect(r);
                        //holder.frameLayout.getChildAt(i).getGlobalV)
                        if (r.contains((int) x, (int) y)) {
                            Log.d("motion_entered", String.valueOf(i));
                        }
                    }
                }
                return true;
            }
        });
        /*holder.frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x;
                float y;
                String sDown = "";
                String sMove = "";
                String sUp = "";
                x = event.getX();
                y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        sDown = "Down: " + x + "," + y;
                        sMove = "";
                        sUp = "";
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        sMove = "Move: " + x + "," + y;
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                        Log.d("motion", "отпустила");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        sMove = "";
                        sUp = "Up: " + x + "," + y;
                        break;
                }
                Log.d("motion", "" + sDown + "\n" + sMove + "\n" + sUp);
                return true;
            }
        });*/
    }

    public void addCategory(Category category){
        categories.add(category);
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
            }
        }

    }

