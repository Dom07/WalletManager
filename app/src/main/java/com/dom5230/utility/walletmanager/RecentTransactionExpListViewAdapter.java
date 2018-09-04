package com.dom5230.utility.walletmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class RecentTransactionExpListViewAdapter extends BaseExpandableListAdapter {
    ArrayList<String> dates;
    HashMap<String, ArrayList<TransactionRecord>> items;
    Context context;

    public RecentTransactionExpListViewAdapter(Context context, ArrayList<String> dates, HashMap<String, ArrayList<TransactionRecord>> items){
        this.context = context;
        this.dates = dates;
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return dates.size();
    }

    @Override
    public int getChildrenCount(int position) {
        return items.get(getGroup(position)).size();
    }

    @Override
    public Object getGroup(int position) {
        return dates.get(position);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        return items.get(dates.get(parentPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean b, View view, ViewGroup viewGroup) {
        // code for group rows
        ArrayList<TransactionRecord> records = items.get(dates.get(position));
        TransactionRecord record = records.get(0);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.recent_transaction_row, null);

        TextView transactionDate = view.findViewById(R.id.tvRecentDate);
        TextView transactionDay = view.findViewById(R.id.tvRecentDay);
        TextView transactionTotalAmount = view.findViewById(R.id.tvRecentAmount);

        transactionDate.setText(record.getDate());
        transactionDay.setText(record.getDayOfWeek());
        DecimalFormat df = new DecimalFormat("0.00");
        Float formattedValue = Float.valueOf(df.format(getTotalAmount(records)));
        transactionTotalAmount.setText(context.getResources().getString(R.string.dollar)+" "+String.valueOf(formattedValue));
        return view;
    }

    @Override
    public View getChildView(int parentPosition, int chilPosition, boolean b, View view, ViewGroup viewGroup) {
        // code for items in group
        ArrayList<TransactionRecord> records = items.get(dates.get(parentPosition));
        TransactionRecord record = records.get(chilPosition);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.transaction_history_row_content, null);

        TextView transactionCategory = view.findViewById(R.id.tvPerItemCategory);
        TextView transactionTime = view.findViewById(R.id.tvPerItemTime);
        TextView transactionDescription = view.findViewById(R.id.tvPerItemDescription);
        TextView transactionAmount = view.findViewById(R.id.tvPerItemAmount);

        transactionCategory.setText(record.getCategory());
        transactionTime.setText(record.getTimeInAmPmFormat());
        transactionDescription.setText(record.getDescription());
        transactionAmount.setText(context.getResources().getString(R.string.dollar)+" "+record.getAmouont());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public float getTotalAmount(ArrayList<TransactionRecord> records){
        float total = 0;
        for(int i =0; i <records.size();i++){
            TransactionRecord record = records.get(i);
            total = total + Float.valueOf(record.getAmouont());
        }
        return total;
    }
}
