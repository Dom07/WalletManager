package com.dom5230.utility.walletmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class TransactionHistory extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        String[] mode ={"CREDIT","CREDIT","DEBIT"};
        float[] amount = {100,200,100};
        String[] reason = {"Salary","Bonus","Shopping"};
        ListView lv = view.findViewById(R.id.lvTransactionsHistory);
        TransactionHistoryAdapter ta = new TransactionHistoryAdapter(getActivity(),mode,amount,reason);
        lv.setAdapter(ta);
        return view;
    }
}