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
                String date = helper.getTodaysOrYesterdaysDate(0);
                String[] sevenDates = prepareSevenDayDates(date);
                records = prepareSevenDayRecords(sevenDates, temprecords);
                total = prepareTotal(records, categories);
                break;
            }

            case "All Time":{
                records = prepareAllRecordsData(temprecords);
                total = prepareTotal(records, categories);
                break;
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

    public ArrayList<TransactionRecord> prepareSevenDayRecords(String[] dates, ArrayList<TransactionRecord> tempRecords){
        ArrayList<TransactionRecord> records = new ArrayList<>();
        for(int i = 0 ; i < tempRecords.size(); i++){
            TransactionRecord record = tempRecords.get(i);
                for(int j = 0; j < dates.length; j++){
                    if(record.getDate().equals(dates[j])){
                        records.add(record);
                    }
                }
        }
        return records;
    }

    public ArrayList<TransactionRecord> prepareAllRecordsData(ArrayList<TransactionRecord> tempRecords) {
        ArrayList<TransactionRecord> records = new ArrayList<>();
        for(int i = 0 ; i < tempRecords.size(); i++){
            TransactionRecord record = tempRecords.get(i);
            records.add(record);
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

//    Month and Days
//    1 - 31
//    2 - 28
//    3 - 31
//    4 - 30
//    5 - 31
//    6 - 30
//    7 - 31
//    8 - 31
//    9 - 30
//    10 - 31
//    11 - 30
//    12 - 31

    public String[] prepareSevenDayDates(String date){
        String[] tempDates = new String[7];
        for(int i = 0; i < 7; i++){
            String[] dateSplit = date.split("/");
            int day = Integer.parseInt(dateSplit[0])-i;
            int month = Integer.parseInt(dateSplit[1]);
            if(day == 0){
                if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 ){
                    day = 31;
                }else{
                    day = 30;
                }
                month = month - 1;
                if(month == 0){
                    month = 12;
                }
            }
            tempDates[i] = String.valueOf(day)+"/"+String.valueOf(month)+"/"+dateSplit[2];
        }
        return tempDates;
    }
}
