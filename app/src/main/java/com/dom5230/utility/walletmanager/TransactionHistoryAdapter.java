package com.dom5230.utility.walletmanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 11/3/18.
 */

public class TransactionHistoryAdapter extends ArrayAdapter {

    Activity context;
    ArrayList<String> dates;
    TextView tvDate, tvDay, tvTotalAmount;
    ListView lvTransactionPerDay;

    public TransactionHistoryAdapter(Activity context, ArrayList<String> dates){
        super(context,R.layout.last_two_transaction_row,dates);
        this.context = context;
        this.dates = dates;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.last_two_transaction_row,null,true);

        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        ArrayList<TransactionRecord> transactionRecordItems = helper.getRowsAsArrayListObjects(context);

        ArrayList<TransactionRecord> transactionRecordPerDay = new ArrayList<>();

        for(int i = 0; i < transactionRecordItems.size(); i++){
            TransactionRecord record = transactionRecordItems.get(i);
            if(record.getDate().equals(dates.get(position))){
                transactionRecordPerDay.add(record);
            }
        }
        tvDate.setText(String.valueOf(dates.get(position)));
        TransactionPerDayItemAdapter transactionPerDayItemAdapter = new TransactionPerDayItemAdapter(context, transactionRecordPerDay, dates.get(position));
        lvTransactionPerDay.setAdapter(transactionPerDayItemAdapter);
        return rowView;
    }
}
