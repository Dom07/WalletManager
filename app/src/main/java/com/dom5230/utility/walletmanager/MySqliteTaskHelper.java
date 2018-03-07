package com.dom5230.utility.walletmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 7/3/18.
 */

public class MySqliteTaskHelper extends SQLiteOpenHelper {

    private static MySqliteTaskHelper sqliteTaskHelperInstance = null;
    private SQLiteDatabase db;

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
        db.execSQL("CREATE TABLE MAIN_BALANCE (NAME TEXT, BALANCE NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS MAIN_BALANCE");
        onCreate(db);
    }

    public void onCredit(Float money, Context context){
        if(checkIfRowExists(context)==0){
            firstCredit(context, money);
        }else {
            float currentBalance = getCurrentBalance(context);
            currentBalance+=money;
            sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
            db = sqliteTaskHelperInstance.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("BALANCE", currentBalance);
            db.update("MAIN_BALANCE", values, "NAME ='TOTAL'", null);
            close(db, sqliteTaskHelperInstance);
        }
    }

    public void firstCredit(Context context, float amount){
        if(checkIfRowExists(context) == 0) {
            sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
            db = sqliteTaskHelperInstance.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NAME", "TOTAL");
            values.put("BALANCE", amount);
            db.insert("MAIN_BALANCE", null, values);
            Log.d("SQL","First Row Inserted");
            close(db,sqliteTaskHelperInstance);
        }else{
            Log.d("SQL", "Row Exists");
        }
    }

    public int checkIfRowExists(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query("MAIN_BALANCE", new String[]{"*"},null,null,null,null,null );
        int row = cursor.getCount();
        Log.d("SQL","Row count ="+row);
        close(db,sqliteTaskHelperInstance);
        return row;
    }

    public float getCurrentBalance(Context context){
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT BALANCE FROM MAIN_BALANCE WHERE NAME = 'TOTAL'",null);
        cursor.moveToFirst();
        float currentBalance = cursor.getFloat(0);
        Log.d("SQL","Current Balance = "+currentBalance);
        close(db,sqliteTaskHelperInstance);
        return currentBalance;
    }


    public void close(SQLiteDatabase db, MySqliteTaskHelper sqliteTaskHelperInstance){
        db.close();
        sqliteTaskHelperInstance.close();
    }
}
