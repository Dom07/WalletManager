package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 11/3/18.
 */

public class TransactionHistoryAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<TransactionHistoryItem> transactionHistoryItems;
    TextView tvTransactionAmount, tvTransactionCategory, tvTransactionDate, tvTransactionTime, tvTransactionDayofWeek;
    TransactionHistoryItem transactionHistoryItem;

    public TransactionHistoryAdapter(Activity context, ArrayList<TransactionHistoryItem> transactionHistoryItems){
        super(context,R.layout.transaction_history_row,transactionHistoryItems);
        this.context = context;
        this.transactionHistoryItems = transactionHistoryItems;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.transaction_history_row,null,true);
        transactionHistoryItem = transactionHistoryItems.get(position);

        tvTransactionAmount = rowView.findViewById(R.id.tvTransactionAmount);
        tvTransactionDate = rowView.findViewById(R.id.TVTransactionDate);
        tvTransactionTime = rowView.findViewById(R.id.TVTransactionTime);
        tvTransactionDayofWeek = rowView.findViewById(R.id.TVDayOfWeek);
        tvTransactionCategory = rowView.findViewById(R.id.tvTransactionCategory);

        tvTransactionDate.setText(transactionHistoryItem.getDate());
        tvTransactionTime.setText(transactionHistoryItem.getTime());
        tvTransactionDayofWeek.setText(transactionHistoryItem.getDayOfWeek());
        tvTransactionCategory.setText(transactionHistoryItem.getCategory());
        tvTransactionAmount.setText(transactionHistoryItem.getAmouont());

        return rowView;
    }
}
