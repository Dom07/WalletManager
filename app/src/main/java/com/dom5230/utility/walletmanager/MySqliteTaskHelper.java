package com.dom5230.utility.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 7/3/18.
 */

public class MySqliteTaskHelper extends SQLiteOpenHelper {

    private static MySqliteTaskHelper sqliteTaskHelperInstance = null;
    private SQLiteDatabase db;
    private TransactionTable table = new TransactionTable();

    public MySqliteTaskHelper(Context context) {
        super(context, "transaction.db",null,1);
    }

    public static MySqliteTaskHelper getInstance(Context context){
        if(sqliteTaskHelperInstance == null){
            sqliteTaskHelperInstance = new MySqliteTaskHelper(context);
        }
        return sqliteTaskHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+table.TABLE_NAME);
        onCreate(db);
    }

    public void insertRow(Context context, String amount, String category){
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        SQLiteDatabase db =  helper.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysofweek = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};


        ContentValues values = new ContentValues();
        values.put(table.DATE, String.valueOf(date+"/"+month+"/"+year));
        values.put(table.TIME, String.valueOf(hour+":"+minutes));
        values.put(table.DAY_OF_WEEK, daysofweek[dayofWeek-1]);
        values.put(table.CATEGORY, category);
        values.put(table.AMOUNT, amount);
        long row = db.insert(table.TABLE_NAME, null, values);
        Log.i("SQL INSERT",String.valueOf(row));
    }

    public ArrayList<TransactionRecord> getRowsAsArrayListObjects(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"},null, null, null, null,null);
        int row = cursor.getCount();
        ArrayList<TransactionRecord> arrayListItems = new ArrayList<TransactionRecord>();
        cursor.moveToFirst();
        for(int i = 0; i<row;i++){
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String dayOfWeek = cursor.getString(3);
            String category = cursor.getString(4);
            String amount = cursor.getString(5);
            TransactionRecord item = new TransactionRecord(date,time,dayOfWeek,category,amount);
            arrayListItems.add(item);
            cursor.moveToNext();
        }
        return arrayListItems;
    }


    public ArrayList<TransactionRecord> getRecentTwoRows(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();

        // using this counter so our for loop only runs 2 times
        int counter = 0;

        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"},null, null, null, null,null);
        int row = cursor.getCount();

        ArrayList<TransactionRecord> arrayListItems = new ArrayList<TransactionRecord>();
        cursor.moveToLast();

        for(int i = row; i > 0; i--){
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String dayOfWeek = cursor.getString(3);
            String category = cursor.getString(4);
            String amount = cursor.getString(5);
            TransactionRecord item = new TransactionRecord(date,time,dayOfWeek,category,amount);
            arrayListItems.add(item);
            if(counter == 1){
                break;
            }
            counter++;
            cursor.moveToPrevious();
        }
        return  arrayListItems;
    }

    public int getExpensesForToday(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        int total = 0;
        Cursor cursor = db.query(table.TABLE_NAME,new String[]{table.AMOUNT},null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String amount = cursor.getString(0);
            total = total + Integer.parseInt(amount);
            cursor.moveToNext();
        }
        return total;
    }




//
//    public int checkIfRowExists(Context context){
//        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
//        db = sqliteTaskHelperInstance.getReadableDatabase();
//        Cursor cursor = db.query("MAIN_BALANCE", new String[]{"*"},null,null,null,null,null );
//        int row = cursor.getCount();
//        Log.d("SQL","Row count ="+row);
//        close(db,sqliteTaskHelperInstance);
//        return row;
//    }
//
//    public float getCurrentBalance(Context context){
//        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
//        db = sqliteTaskHelperInstance.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT BALANCE FROM MAIN_BALANCE WHERE NAME = 'TOTAL'",null);
//        cursor.moveToFirst();
//        float currentBalance = cursor.getFloat(0);
//        Log.d("SQL","Current Balance = "+currentBalance);
//        close(db,sqliteTaskHelperInstance);
//        return currentBalance;
//    }
//
//
//    public void close(SQLiteDatabase db, MySqliteTaskHelper sqliteTaskHelperInstance){
//        db.close();
//        sqliteTaskHelperInstance.close();
//    }
}
