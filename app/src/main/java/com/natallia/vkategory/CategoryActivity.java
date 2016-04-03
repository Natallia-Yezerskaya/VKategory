package com.natallia.vkategory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CategoryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerCategory, new CategoryFragment())
                .commitAllowingStateLoss();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }





    public static class CategoryFragment extends android.support.v4.app.Fragment {
        public CategoryFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_category, container, false);

            // TODO здесь описать действия по нажатию на кнопку create
            return v;
        }

    }

    /**
     * Created by Natallia on 22.03.2016.
     */
    public static class CreateNewCategoryDialog extends DialogFragment {

        public CreateNewCategoryDialogListener mListener;
        public Activity activity;
        public interface CreateNewCategoryDialogListener {
            public void onDialogPositiveClick(DialogFragment dialog, String text);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_set_name, null);
            //((EditText) view.findViewById(R.id.et_dialog)).setText("");
            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String newName = ((EditText) view.findViewById(R.id.et_dialog)).getText().toString();
                    mListener.onDialogPositiveClick(CreateNewCategoryDialog.this, newName);
                }
            })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CreateNewCategoryDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (mListener==null){
                dismiss();
            }
        }
    }
}