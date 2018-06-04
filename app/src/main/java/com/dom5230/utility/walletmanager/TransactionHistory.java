package com.dom5230.utility.walletmanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class TransactionHistory extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        MySqliteTaskHelper helper = new MySqliteTaskHelper(getContext());
        ArrayList<TransactionRecord> items = helper.getRowsAsArrayListObjects(getContext());
        TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(getActivity(),items);
        ListView listView = view.findViewById(R.id.lvTransactionsHistory);
        listView.setAdapter(adapter);

      return view;
    }
}