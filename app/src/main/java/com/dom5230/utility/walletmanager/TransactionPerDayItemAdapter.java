package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionPerDayItemAdapter extends ArrayAdapter {

    Activity context;
    TextView tvCategory, tvTime, tvAmount;
    ArrayList<TransactionRecord> transactionRecordItems;
    TransactionRecord transactionRecord;


    public TransactionPerDayItemAdapter(Activity activity, ArrayList<TransactionRecord> transactionRecordItems, String transactionDate) {
        super(activity, R.layout.transaction_history_row_content, transactionRecordItems);
        this.context = activity;
        this.transactionRecordItems = transactionRecordItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.transaction_history_row_content, null, false);

        tvCategory = rowView.findViewById(R.id.tvPerItemCategory);
        tvTime = rowView.findViewById(R.id.tvPerItemTime);
        tvAmount = rowView.findViewById(R.id.tvPerItemAmount);

        transactionRecord = transactionRecordItems.get(position);

        tvCategory.setText(transactionRecord.getCategory());
        tvTime.setText(transactionRecord.getTime());
        tvAmount.setText(transactionRecord.getAmouont());

        return rowView;
    }
}
