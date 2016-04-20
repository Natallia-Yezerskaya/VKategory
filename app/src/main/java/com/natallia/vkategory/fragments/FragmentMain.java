package com.natallia.vkategory.fragments;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.natallia.vkategory.MainActivity;
import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.CategoryFragmentEventHandler;
import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.database.DataManager;

/**
 * Created by Natallia on 12.04.2016.
 */
public class FragmentMain extends Fragment implements CategoryFragmentEventHandler,PostDraggingListener {
    public static String POST_FRAGMENT_INSTANCE_NAME = "fragmentPost";
    public static String CATEGORY_FRAGMENT_INSTANCE_NAME = "fragmentCategory";
    PostsFragment fragmentPost = null;
    CategoryFragment fragmentCategory = null;
    private FrameLayout.LayoutParams lParams1;
    private int mWidthLayoutCategory;
    FrameLayout layoutCategory;

    public static FragmentMain createFragment() {
        FragmentMain fragment = new FragmentMain();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FragmentManager fm = getChildFragmentManager();
        fragmentCategory = (CategoryFragment) fm.findFragmentByTag(CATEGORY_FRAGMENT_INSTANCE_NAME);
        if(fragmentCategory == null){
            fragmentCategory = new CategoryFragment();
            fragmentCategory.setPostDraggingListener(this);
            fragmentCategory.setCategoryFragmentEventHandler(this);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerCategory, fragmentCategory,CATEGORY_FRAGMENT_INSTANCE_NAME)
                    .commit();
        }

        fragmentCategory.setPostDraggingListener(this);
        fragmentCategory.setCategoryFragmentEventHandler(this);

        layoutCategory = (FrameLayout)view.findViewById(R.id.containerCategory);
        //FrameLayout layoutPosts = (FrameLayout) findViewById(R.id.containerPosts);
        lParams1 = (FrameLayout.LayoutParams) layoutCategory.getLayoutParams();
        mWidthLayoutCategory = lParams1.width;


        fragmentPost = (PostsFragment) fm.findFragmentByTag(POST_FRAGMENT_INSTANCE_NAME);
        if(fragmentPost == null){
            refreshPostsFragment(0);
        }
        fragmentPost.setPostDraggingListener(this);

        return view;
    }


    public void refreshPostsFragment(int idCategory) {
        Intent intent = new Intent();
        intent.putExtra("idCategory", idCategory);
        fragmentPost = PostsFragment.createFragment(intent);
        fragmentPost.setPostDraggingListener(this);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.containerPosts,fragmentPost,POST_FRAGMENT_INSTANCE_NAME)
                .commit();
    }



    @Override
    public boolean OnPostCategoryChange(int postId, int categoryId) {
        boolean replaced  = DataManager.getInstance().replacePostsIntoCategory(postId, categoryId);
        if (replaced) {
            fragmentPost.removePostFromView(postId);
            return true;
        }
        return false;
    }

    @Override
    public void refreshPost(int id) {
        refreshPostsFragment(id);
    }


    @Override
    public void onPostDrag(boolean isDragging) {
        fragmentCategory.CategoryForChoosing(isDragging);

        Resources r = getResources();
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());

        ValueAnimator valueAnimator = ValueAnimator.ofInt(mWidthLayoutCategory, width);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {

                lParams1.width = (int)animation.getAnimatedValue();
                layoutCategory.setLayoutParams(lParams1);
            }
        });

        valueAnimator.setDuration(100);
        if (isDragging){
            valueAnimator.start();
        }
        else{
            valueAnimator.reverse();
        }
    }

    @Override
    public void onPostDetail(int postID) {
        ((MainActivity)getActivity()).openDetailFragment(postID);
    }

    @Override
    public void onPostSlideShow(int postID, int position) {
        ((MainActivity)getActivity()).openSlideShowFragment(postID,position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


}
