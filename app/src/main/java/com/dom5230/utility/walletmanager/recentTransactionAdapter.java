package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class recentTransactionAdapter extends ArrayAdapter {
    Activity context;
    ArrayList<TransactionRecord> transactionRecorditems;
    TextView tvCategory, tvTime, tvAmount;
    TransactionRecord transactionRecorditem;

    public recentTransactionAdapter(Activity context, ArrayList<TransactionRecord> transactionRecorditems){
        super(context, R.layout.recent_transaction_row, transactionRecorditems);
        this.context = context;
        this.transactionRecorditems = transactionRecorditems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.last_two_transaction_row, null, false);
        transactionRecorditem = transactionRecorditems.get(position);

        tvCategory = rowView.findViewById(R.id.tvCategory);
        tvTime = rowView.findViewById(R.id.tvTime);
        tvAmount = rowView.findViewById(R.id.tvAmount);

        tvCategory.setText(transactionRecorditem.getCategory());
        tvTime.setText(transactionRecorditem.getTime());
        tvAmount.setText("Rs. "+transactionRecorditem.getAmouont());

//        tvRecentAmount = rowView.findViewById(R.id.tvRecentAmount);
//        tvRecentCategory = rowView.findViewById(R.id.tvRecentCategory);
//        tvRecentDate = rowView.findViewById(R.id.tvRecentDate);
//
//        tvRecentCategory.setText(transactionRecorditem.getCategory());
//        tvRecentDate.setText(transactionRecorditem.getDate());
//        tvRecentAmount.setText("Rs."+transactionRecorditem.getAmouont());

        return rowView;
    }
}
