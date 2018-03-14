package com.dom5230.utility.walletmanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TransactionHistory extends Fragment {
    ArrayList<TransactionHistoryItem> transactionHistoryItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        ListView lv = view.findViewById(R.id.lvTransactionsHistory);
        TransactionManager tm = new TransactionManager(getContext());
        transactionHistoryItems = tm.getTransactionHistory();
        if(transactionHistoryItems != null){
        TransactionHistoryAdapter ta = new TransactionHistoryAdapter(getActivity(),transactionHistoryItems);
        lv.setAdapter(ta);
        }else{
            Toast.makeText(getContext(),"No History Exist",Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}