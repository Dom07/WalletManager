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
    TextView tvTransactionMode, tvTransactionAmount, tvTransactionBalance, tvTimeStamp;
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

        tvTransactionMode = rowView.findViewById(R.id.tvTransactionMode);
        tvTransactionAmount = rowView.findViewById(R.id.tvTransactionAmount);
        tvTransactionBalance = rowView.findViewById(R.id.tvTransactionBalance);
        tvTimeStamp = rowView.findViewById(R.id.tvTimeStamp);

        tvTransactionMode.setText(transactionHistoryItem.getTrans_mode());
        tvTransactionAmount.setText(transactionHistoryItem.getAmount());
        tvTransactionBalance.setText(transactionHistoryItem.getBalance());
        tvTimeStamp.setText(transactionHistoryItem.getTimestamp());

        return rowView;
    }
}
