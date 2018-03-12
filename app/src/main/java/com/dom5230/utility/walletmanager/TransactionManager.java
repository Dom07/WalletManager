package com.dom5230.utility.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;

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

    private void processTransaction(String transactionMode, float amount, MySqliteTaskHelper helper){
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
        insertTransactionRecord(currentBalance, amount, db, transactionMode);
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

    private void firstCredit(Context context, float amount){
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

    private void insertTransactionRecord(float currentBalance, float amount, SQLiteDatabase db, String transactionMode){
        ContentValues values = new ContentValues();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        values.put("TIMESTAMP", String.valueOf(timestamp));
        System.out.println("TIMESTAMP: "+timestamp);
        values.put("TRANS_MODE", transactionMode);
        values.put("AMOUNT", String.valueOf(amount));
        values.put("BALANCE", currentBalance);
        db.insert("TRANS_HISTORY",null,values);
    }

    public void getTransactionHistory() {
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        String[] PROJECTION = {"TIMESTAMP", "TRANS_MODE", "AMOUNT", "BALANCE"};
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("TRANS_HISTORY", PROJECTION, null, null, null, null, null);
        int i = 1;
        if (cursor == null) {
            Log.d("SQL_TEST", "TRANSACTION TABLE EMPTY");
        } else {
            while (cursor.moveToNext()) {
                String timestamp = cursor.getString(0);
                String trans_mode = cursor.getString(1);
                String amount = cursor.getString(2);
                String balance = cursor.getString(3);
                System.out.println("ROW No:" + i);
                Log.d("SQL_TEST", "Time Stamp : " + timestamp + ", Trans_Mode: " + trans_mode + " ,Amount: " + amount + " ,Balance: " + balance);
                i++;
            }
            helper.close(db, helper);
        }
    }
}
