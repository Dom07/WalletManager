package com.dom5230.utility.walletmanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionHistory extends Fragment {
    ArrayList<String> dates;
    HashMap<String, ArrayList<TransactionRecord>> items;
    MySqliteTaskHelper sqliteTaskHelper;

    TextView tvNoData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        tvNoData = view.findViewById(R.id.tvNoData);
        sqliteTaskHelper = MySqliteTaskHelper.getInstance(getContext());
        dates = new ArrayList<>();
        items = new HashMap<>();

        dates = getDatesFromDb();
        fillHashMap(dates);

        ExpandableListView expandableListView = view.findViewById(R.id.elvTransactionHistory);
        RecentTransactionExpListViewAdapter adapter = new RecentTransactionExpListViewAdapter(getContext(), dates, items);
        expandableListView.setAdapter(adapter);
        updateTextViewVisibilityIfNoData();
        return view;
    }

    public ArrayList<String> getDatesFromDb(){
        return sqliteTaskHelper.getDates(getContext());
    }

    public void fillHashMap(ArrayList<String> dates){
        ArrayList<TransactionRecord> records = sqliteTaskHelper.getRowsAsArrayListObjects(getContext());
        for(int i = dates.size()-1; i >=0; i--){
            ArrayList<TransactionRecord> confirmedRecords = new ArrayList<>();
            for(int j = records.size()-1; j>=0; j--){
                TransactionRecord record = records.get(j);
                if(dates.get(i).equals(record.getDate())){
                    confirmedRecords.add(record);
                }
            }
            if(confirmedRecords.size() != 0) {
                items.put(dates.get(i), confirmedRecords);
            }
        }
    }

    public void updateTextViewVisibilityIfNoData(){
        ArrayList<String> itemDates = sqliteTaskHelper.getDates(getContext());
        if(itemDates.size() == 0){
            tvNoData.setVisibility(View.VISIBLE);
        }else{
            tvNoData.setVisibility(View.INVISIBLE);
        }
    }
}