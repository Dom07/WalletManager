package com.dom5230.utility.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by root on 9/3/18.
 */

public class TransactionManager {

    Context context;
    MySqliteTaskHelper helper;
    SQLiteDatabase db;
    private final String CREDIT = "CREDIT", DEBIT = "DEBIT";

    public TransactionManager(Context context){
        this.context = context;
    }

    public void processTransaction(String transactionMode, float amount, MySqliteTaskHelper helper){
        float currentBalance = helper.getCurrentBalance(context);
        Log.d("SQL","CurrentBalance : Rs."+currentBalance);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(transactionMode.equals(CREDIT)){
            currentBalance+=amount;
            Log.d("SQL","After Credit, CurrentBalance : Rs."+currentBalance);
        }else{
            currentBalance-=amount;
            Log.d("SQL","After Debit, CurrentBalance : Rs."+currentBalance);
        }
        ContentValues values = new ContentValues();
        values.put("BALANCE", currentBalance);
        db.update("MAIN_BALANCE", values, "NAME ='TOTAL'", null);
        helper.close(db,helper);
    }

    public void initiateTransaction(Float amount, String transactionMode){
        helper = MySqliteTaskHelper.getInstance(context);
        if(transactionMode.equals(CREDIT)){
            if(helper.checkIfRowExists(context)==0)
                firstCredit(context, amount);
            else{
                processTransaction(transactionMode, amount, helper);
            }
        }
        else if(transactionMode.equals(DEBIT)){
            if(helper.checkIfRowExists(context)==0)
                Toast.makeText(context,"Nothing to Debit",Toast.LENGTH_SHORT).show();
            else{
                processTransaction(transactionMode, amount, helper);
            }
        }
    }

    public void firstCredit(Context context, float amount){
        if(helper.checkIfRowExists(context) == 0) {
            helper = MySqliteTaskHelper.getInstance(context);
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", "TOTAL");
            values.put("BALANCE", amount);
            db.insert("MAIN_BALANCE", null, values);
            Log.d("SQL","First Row Inserted");
        }else{
            Log.d("SQL", "Row Exists");
        }
        helper.close(db,helper);
    }


}
