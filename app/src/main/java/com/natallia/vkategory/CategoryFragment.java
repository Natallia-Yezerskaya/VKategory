package com.natallia.vkategory;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.natallia.vkategory.UI.CategoryFragmentEventHandler;
import com.natallia.vkategory.adapters.CategoryAdapter;
import com.natallia.vkategory.database.DataManager;
import com.natallia.vkategory.database.DataManager1;
import com.natallia.vkategory.models.Category;

import java.util.List;

/**
 * Created by Natallia on 22.03.2016.
 */
public class CategoryFragment  extends Fragment {

    static final int PICK_CONTACT_REQUEST = 1;


    private Button btnCreate;
    private ListView listView;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private CategoryFragmentEventHandler mCategoryFragmentEventHandler;

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


        btnCreate = (Button) view.findViewById(R.id.createCategory);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvCategory);
        // открываем "Контакты" при нажатии на кнопку
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
                //((MainActivity) getActivity()).dataManager.addCategory("Add Category");

            }
        });

        //TODO переписать на recyclerView


        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvCategory);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Category> values = DataManager.getInstance().getCategoriesList();
        mAdapter = new CategoryAdapter(values,getActivity());
        mAdapter.setCategoryFragmentEventHandler(mCategoryFragmentEventHandler);
        //mAdapter.setOnEditorActionListener(this);
       // mRecyclerView.setItemAnimator(null);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //List<String> categories =  ((MainActivity) getActivity()).dataManager.getCategoriesListString();
        //ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1,categories);
        //listView.setAdapter(arrayAdapter);
       // mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                ((MainActivity) getActivity()).refreshPostsFragment(position);
//
//                Log.d("myTag", "itemClick: position = " + position + ", id = "
//                        + id);
//            }
//        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //todo вставляем добавленную категорию

    }
}
