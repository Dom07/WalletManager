package com.dom5230.utility.walletmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ManageCategoriesAdapter extends RecyclerView.Adapter<ManageCategoriesAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> categoriesList;

    public ManageCategoriesAdapter(Context context, ArrayList<String> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public ManageCategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_category_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCategoriesAdapter.MyViewHolder holder, int position) {
        String category = categoriesList.get(position);
        holder.tvCategoryItem.setText(category);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCategoryItem = itemView.findViewById(R.id.tvCategoryItem);
        }
    }
}
