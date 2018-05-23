package com.dom5230.utility.walletmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends Fragment {

//    public final MySqliteTaskHelper helper = new MySqliteTaskHelper(getContext());
//    private final String CREDIT = "CREDIT", DEBIT = "DEBIT";
//    private TransactionManager tm;
//
//    private TextView tvBalance;
//    private Button btnCredit;
//    private Button btnDebit;
//    private EditText etAmount;

    FloatingActionButton fab;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fab = view.findViewById(R.id.FAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(getContext());
                helper.insertRow(getContext());
                helper.getRows(getContext());
            }
        });

//        tvBalance = view.findViewById(R.id.tvBalance);
//        btnCredit = view.findViewById(R.id.btnCredit);
//        btnDebit = view.findViewById(R.id.btnDebit);
//        etAmount = view.findViewById(R.id.etAmount);
//
//        tm = new TransactionManager(context);
//
//        updateTextVBalance(tvBalance);
//
//        btnCredit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                float amount = Float.parseFloat(getEditTextAmount());
//                   tm.initiateTransaction(amount,CREDIT);
//
//
////                   just testing the transaction history function
//                   tm.getTransactionHistory();
//
//
//
//                    updateTextVBalance(tvBalance);
//            }
//        });
//
//        btnDebit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                float amount = Float.parseFloat(getEditTextAmount());
//                tm.initiateTransaction(amount,DEBIT);
//                updateTextVBalance(tvBalance);
//            }
//        });
        return view;
    }
//    public void updateTextVBalance(TextView tvBalance){
//        if(helper.checkIfRowExists(getContext())== 0){
//            tvBalance.setText("Rs.0");
//        }else {
//            String balance = String.valueOf(helper.getCurrentBalance(context));
//            tvBalance.setText("Rs."+balance);
//        }
//    }
//
//    private String getEditTextAmount(){
//        return etAmount.getText().toString().trim();
//    }
}
