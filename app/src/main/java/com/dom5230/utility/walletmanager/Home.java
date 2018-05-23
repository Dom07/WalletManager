package com.dom5230.utility.walletmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Home extends Fragment {

    // Objects
    private MySqliteTaskHelper helper;

    // UI Components
    FloatingActionButton fab;
    ListView lvLastFiveTransactions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        helper = MySqliteTaskHelper.getInstance(getContext());

        fab = view.findViewById(R.id.FAB);
        lvLastFiveTransactions = view.findViewById(R.id.LVLastFiveTransacctions);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mview = getLayoutInflater().inflate(R.layout.expense_input_dailogue,null);
                final EditText ETAmount = mview.findViewById(R.id.ETAmount);
                final Spinner spinner = mview.findViewById(R.id.SpinnerCategory);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.insertRow(getContext(), ETAmount.getText().toString(),spinner.getSelectedItem().toString());
                    }
                });
                mBuilder.setNegativeButton("Cancel", null);
                mBuilder.setView(mview);
                mBuilder.setTitle("Expenditure Details");
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        ArrayList<TransactionHistoryItem> items = helper.getRowsAsArrayListObjects(getContext());
        TransactionHistoryAdapter adapter1 = new TransactionHistoryAdapter(getActivity(), items);
        lvLastFiveTransactions.setAdapter(adapter1);

        return view;
    }
}
