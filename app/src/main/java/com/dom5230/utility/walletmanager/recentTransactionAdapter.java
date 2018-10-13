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
    TextView tvCategory, tvDescription, tvAmount;
    TransactionRecord transactionRecorditem;
    String currency = MainActivity.getCurrency();

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
        tvDescription = rowView.findViewById(R.id.tvDescription);
        tvAmount = rowView.findViewById(R.id.tvAmount);

        tvCategory.setText(transactionRecorditem.getCategory());
        tvDescription.setText(transactionRecorditem.getDescription());
        tvAmount.setText(currency+" "+transactionRecorditem.getAmouont());

        return rowView;
    }
}
