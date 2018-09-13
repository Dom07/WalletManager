package com.dom5230.utility.walletmanager;

import android.content.Context;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class MyPieChart {
    Context context;
    ArrayList<TransactionRecord> records;
    float[] total;

    public MyPieChart(Context context) {
        this.context = context;
    }

    public ArrayList<PieEntry> preparePieData(String timeLine){
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);

        ArrayList<TransactionRecord> temprecords = helper.getRowsAsArrayListObjects(context);
        ArrayList<String> categories = helper.getCategoriesList(context);

        switch(timeLine){
            case "Today":{
                String date = helper.getTodaysOrYesterdaysDate(0);
                records = prepareRecords(date, temprecords);
                total = prepareTotal(records, categories);
                break;
            }

            case "Yesterday":{
                String date = helper.getTodaysOrYesterdaysDate(1);
                records = prepareRecords(date, temprecords);
                total = prepareTotal(records, categories);
                break;
            }

            case "Last Week":{
                break;
            }

            case "All Time":{
                break;
            }
        }

//        for(int i = 0 ; i < temprecords.size(); i++){
//            TransactionRecord record = temprecords.get(i);
//            if(record.getDate().equals(date)){
//                records.add(record);
//            }
//        }

//
//        for(int i = 0; i < records.size();i++){
//            TransactionRecord record = records.get(i);
//                for(int count = 0; count<categories.size(); count++){
//                    if(categories.get(count).equals(record.getCategory())){
//                        total[count]+=Float.valueOf(record.getAmouont());
//                    }
//                }
//        }

        for(int i =0; i<total.length;i++){
            if(total[i]!=0) {
                PieEntry pieEntry = new PieEntry(total[i], categories.get(i));
                pieEntries.add(pieEntry);
            }
        }

        return pieEntries;
    }

    public ArrayList<TransactionRecord> prepareRecords(String date, ArrayList<TransactionRecord> temprecords){
        ArrayList<TransactionRecord> records = new ArrayList<>();
        for(int i = 0 ; i < temprecords.size(); i++){
            TransactionRecord record = temprecords.get(i);
            if(record.getDate().equals(date)){
                records.add(record);
            }
        }
        return records;
    }

    public float[] prepareTotal(ArrayList<TransactionRecord> records, ArrayList<String> categories){
        float[] total = new float[categories.size()];
        for(int i = 0; i < records.size();i++){
            TransactionRecord record = records.get(i);
            for(int count = 0; count<categories.size(); count++){
                if(categories.get(count).equals(record.getCategory())){
                    total[count]+=Float.valueOf(record.getAmouont());
                }
            }
        }
        return total;
    }
}
