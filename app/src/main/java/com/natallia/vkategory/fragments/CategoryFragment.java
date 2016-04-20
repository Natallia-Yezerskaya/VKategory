package com.natallia.vkategory.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.natallia.vkategory.CategoryActivity;
import com.natallia.vkategory.R;
import com.natallia.vkategory.UI.CategoryFragmentEventHandler;

import com.natallia.vkategory.UI.PostDraggingListener;
import com.natallia.vkategory.adapters.CategoryAdapter;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.models.Category;

import java.util.List;

/**
 * Created by Natallia on 22.03.2016.
 */
public class CategoryFragment  extends Fragment {

    static final int PICK_CONTACT_REQUEST = 1;
    static final String TAG = "CATEGORY_FRAGMENT";



    private FloatingActionButton btnCreate;
   // private ListView listView;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    //private FrameLayout mCardView;
    private CategoryFragmentEventHandler mCategoryFragmentEventHandler;
    private int idSelectedCategory;

    private PostDraggingListener postDraggingListener;

    public PostDraggingListener getPostDraggingListener() {
        return postDraggingListener;
    }

    public void setPostDraggingListener(PostDraggingListener postDraggingListener) {
        this.postDraggingListener = postDraggingListener;
    }

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

    public static CategoryFragment getInstance( Intent intent) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        if (savedInstanceState != null) {
            idSelectedCategory = savedInstanceState.getInt("idSelectedCategory", 0);
        }

        btnCreate = (FloatingActionButton) view.findViewById(R.id.fab);
        btnCreate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent2)));

        //btnCreate.setBackground();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvCategory);
        // mCardView = (FrameLayout) view.findViewById(R.id.frame_layout);
        // mCardView.setMaxCardElevation(32f);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CategoryActivity.CreateNewCategoryDialog myDialogFragment = new CategoryActivity.CreateNewCategoryDialog();
                myDialogFragment.show(getActivity().getFragmentManager(), "CreateNewCategoryDialog");
                //обработка нажатия клавиши "Сохранить" в диалоге
                myDialogFragment.mListener = new CategoryActivity.CreateNewCategoryDialog.CreateNewCategoryDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog, String text) {
                        if (!text.equals("")) {
                            Category category = DataManager.getInstance().addCategory(text);
                            mAdapter.addCategory(category);
                            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
                        }
                    }
                };

            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvCategory);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Category> values = DataManager.getInstance().getCategoriesList();
        mAdapter = new CategoryAdapter(values,getActivity());
        mAdapter.setIdSelectedCategory(idSelectedCategory);
        mAdapter.setPostDraggingListener(postDraggingListener);
        mAdapter.setCategoryFragmentEventHandler(mCategoryFragmentEventHandler);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        Log.d(TAG, "onCreateView: ");
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            

        }
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        if (savedInstanceState != null) {
            idSelectedCategory = savedInstanceState.getInt("idSelectedCategory", 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void CategoryForChoosing(boolean forChoosing) {
        mAdapter.categoryForChoosing(forChoosing);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        if (mAdapter!=null){ //TODO посмотреть
            idSelectedCategory = mAdapter.getIdSelectedCategory();
        }

        outState.putInt("idSelectedCategory", idSelectedCategory);
    }
}
