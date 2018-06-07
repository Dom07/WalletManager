package com.dom5230.utility.walletmanager;

import android.content.Context;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MyPieChart {
    Context context;
    public MyPieChart(Context context) {
        this.context = context;
    }

    public ArrayList<PieEntry> preparePieData(){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        ArrayList<TransactionRecord> records = helper.getRowsAsArrayListObjects(context);
        ArrayList<String> categories = helper.getCategoriesList(context);
        int[] total = new int[categories.size()];

        for(int i = 0; i < records.size();i++){
            TransactionRecord record = records.get(i);
                for(int count = 0; count<categories.size(); count++){
                    if(categories.get(count).equals(record.getCategory())){
                        total[count]+=Integer.parseInt(record.getAmouont());
                    }
                }
        }

        for(int i =0; i<total.length;i++){
            if(total[i]!=0) {
                PieEntry pieEntry = new PieEntry(total[i], categories.get(i));
                pieEntries.add(pieEntry);
            }
        }

        return pieEntries;
    }
}
