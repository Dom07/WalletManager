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

import java.util.ArrayList;


public class ManageCategories extends Fragment {
    ArrayList<String> categoriesList = new ArrayList<>();
    ManageCategoriesAdapter adapter;
    RecyclerView rvCategories;

    MySqliteTaskHelper helper;

    public ManageCategories() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        helper = MySqliteTaskHelper.getInstance(getContext());

        categoriesList = helper.getCategoriesList(getContext());
        Log.d("Categories List", String.valueOf(categoriesList));

        adapter = new ManageCategoriesAdapter(getContext(), categoriesList);

        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setAdapter(adapter);

        return  view;
    }
}
