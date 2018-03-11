package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by root on 11/3/18.
 */

public class TransactionHistoryAdapter extends ArrayAdapter {

    Activity context;
    String[] transactionMode;
    float[] amount;
    String[] reason;
    TextView tvTransactionMode, tvTransactionAmount, tvTransactionReason;

    public TransactionHistoryAdapter(Activity context, String[] transactionMode, float[] amount, String[] reason){
        super(context,R.layout.transaction_history_row,transactionMode);
        this.context = context;
        this.transactionMode = transactionMode;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.transaction_history_row,null,true);
        tvTransactionMode = rowView.findViewById(R.id.tvTransactionMode);
        tvTransactionAmount = rowView.findViewById(R.id.tvTransactionAmount);
        tvTransactionReason = rowView.findViewById(R.id.tvTransactionReason);
        tvTransactionMode.setText(transactionMode[position]);
        tvTransactionAmount.setText(String.valueOf(amount[position]));
        tvTransactionReason.setText(reason[position]);
        return rowView;
    }
}
