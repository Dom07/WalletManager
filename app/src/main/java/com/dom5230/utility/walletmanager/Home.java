package com.dom5230.utility.walletmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends Fragment {

    public Context context = getContext();
    public final MySqliteTaskHelper helper = new MySqliteTaskHelper(context);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        boolean flag = false;
        final TextView tvBalance = (TextView)view.findViewById(R.id.tvBalance);
        Button btnCredit = (Button)view.findViewById(R.id.btnCredit);
        final EditText etAmount = (EditText)view.findViewById(R.id.etAmount);

        updateTextVBalance(tvBalance);

        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountEntered = etAmount.getText().toString().trim();
                float floatamount = Float.parseFloat(amountEntered);
                helper.onCredit(floatamount,getContext());
                updateTextVBalance(tvBalance);
            }
        });
        return view;
    }

    public void updateTextVBalance(TextView tvBalance){
        if(helper.checkIfRowExists(getContext())== 0){
            tvBalance.setText("Rs.0");
        }else {
            float currentBalance = helper.getCurrentBalance(getContext());
            String balance = String.valueOf(currentBalance);
            tvBalance.setText(balance);
        }
    }
}
