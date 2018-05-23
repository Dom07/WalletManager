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
        ContentValues values = new ContentValues();
        values.put(table.DATE, Calendar.DATE);
        values.put(table.TIME, "10:16");
        values.put(table.DAY_OF_WEEK, Calendar.DAY_OF_WEEK);
        values.put(table.CATEGORY, category);
        values.put(table.AMOUNT, amount);
        long row = db.insert(table.TABLE_NAME, null, values);
        Log.i("SQL INSERT",String.valueOf(row));
    }

    public ArrayList<TransactionHistoryItem> getRowsAsArrayListObjects(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"},null, null, null, null,null);
        int row = cursor.getCount();
        ArrayList<TransactionHistoryItem> arrayListItems = new ArrayList<TransactionHistoryItem>();
        cursor.moveToFirst();
        for(int i = 0; i<row;i++){
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String dayOfWeek = cursor.getString(3);
            String category = cursor.getString(4);
            String amount = cursor.getString(5);
            TransactionHistoryItem item = new TransactionHistoryItem(date,time,dayOfWeek,category,amount);
            arrayListItems.add(item);
            cursor.moveToNext();
        }
        return arrayListItems;
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
