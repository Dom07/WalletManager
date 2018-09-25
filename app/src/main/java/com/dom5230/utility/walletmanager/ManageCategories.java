package com.dom5230.utility.walletmanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;


public class ManageCategories extends Fragment {
    ArrayList<String> categoriesList = new ArrayList<>();
    ManageCategoriesAdapter adapter;
    RecyclerView rvCategories;

    MySqliteTaskHelper helper;

    public ManageCategories() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);


        helper = MySqliteTaskHelper.getInstance(getContext());

        categoriesList = helper.getCategoriesList(getContext());
        Log.d("Categories List", String.valueOf(categoriesList));

        adapter = new ManageCategoriesAdapter(getContext(), categoriesList);
        updateRecyclerView(view);

        return  view;
    }

    public void updateRecyclerView(View view){
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.manage_categories_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add_new_category){
            addNewCategoryAlertBox();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_manage_categories);
        item.setVisible(false);
    }

        public void addNewCategoryAlertBox(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mview = getLayoutInflater().inflate(R.layout.dialogue_add_new_category,null);

        final EditText etAddNewCategory = mview.findViewById(R.id.etNewCategory);

        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String category = etAddNewCategory.getText().toString().trim();
                MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(getContext());
                helper.insertNewCategory(getContext(), category);
                categoriesList.add(category);
                adapter.notifyDataSetChanged();
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        mBuilder.setView(mview);
        mBuilder.setTitle("Add New Category");
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
