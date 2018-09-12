package com.dom5230.utility.walletmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class ManageCategories extends Fragment {
    ArrayList<String> categoriesList = new ArrayList<>();
    ManageCategoriesAdapter adapter;
    RecyclerView rvCategories;
    EditText etCategoryItem;
    ImageView ivAddCategory;

    MySqliteTaskHelper helper;

    public ManageCategories() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        etCategoryItem = view.findViewById(R.id.etCategoryItem);
        ivAddCategory = view.findViewById(R.id.ivAddCategory);

        helper = MySqliteTaskHelper.getInstance(getContext());

        categoriesList = helper.getCategoriesList(getContext());
        Log.d("Categories List", String.valueOf(categoriesList));

        adapter = new ManageCategoriesAdapter(getContext(), categoriesList);
        updateRecyclerView(view);

        ivAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = etCategoryItem.getText().toString();
                if(category.equals("")){
                    Toast.makeText(getContext(), "Nothing to add!",Toast.LENGTH_SHORT).show();
                }else{
                    MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(getContext());
                    helper.insertNewCategory(getContext(), category);
                    categoriesList.add(category);
                    adapter.notifyDataSetChanged();
                    etCategoryItem.setText("");
                    etCategoryItem.clearFocus();
                }
            }
        });
        return  view;
    }

    public void updateRecyclerView(View view){
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setAdapter(adapter);
    }

//    public void addNewCategoryAlertBox(){
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
//        View mview = getLayoutInflater().inflate(R.layout.dialogue_add_new_category,null);
//
//        final EditText etAddNewCategory = mview.findViewById(R.id.etNewCategory);
//
//        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String category = etAddNewCategory.getText().toString().trim();
//                MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(getContext());
//                helper.insertNewCategory(getContext(), category);
//
//            }
//        });
//
//        mBuilder.setNegativeButton("Cancel", null);
//        mBuilder.setView(mview);
//        mBuilder.setTitle("Add New Category");
//        AlertDialog dialog = mBuilder.create();
//        dialog.show();
//    }
}
