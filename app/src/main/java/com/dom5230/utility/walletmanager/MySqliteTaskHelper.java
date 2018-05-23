package com.dom5230.utility.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public void insertRow(Context context){
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        SQLiteDatabase db =  helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(table.DATE, Calendar.DATE);
        values.put(table.TIME, "10:16");
        values.put(table.DAY_OF_WEEK, Calendar.DAY_OF_WEEK);
        values.put(table.CATEGORY, "Food");
        values.put(table.AMOUNT, "3000");
        long row = db.insert(table.TABLE_NAME, null, values);
        Log.i("SQL INSERT",String.valueOf(row));
    }

    public void getRows(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"},null, null, null, null,null);
        int row = cursor.getCount();
        cursor.moveToFirst();
        for(int i = 0; i<row;i++){
            String Date =cursor.getString(0);
            String Time = cursor.getString(2);
            String DayOfWeek = cursor.getString(3);
            String Category = cursor.getString(4);
            String Amount = cursor.getString(5);
            Log.i("SQL ROW "+i+" :","Date:"+Date+" Time:"+Time+" DOW:"+DayOfWeek+" Category:"+Category+" Amount:"+Amount);
            cursor.moveToNext();
        }
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
