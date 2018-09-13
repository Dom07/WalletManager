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
    private CategoryTable categoryTable = new CategoryTable();

    public MySqliteTaskHelper(Context context) {
        super(context, "transaction.db", null, 1);
    }

    public static MySqliteTaskHelper getInstance(Context context) {
        if (sqliteTaskHelperInstance == null) {
            sqliteTaskHelperInstance = new MySqliteTaskHelper(context);
        }
        return sqliteTaskHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table.CREATE_TABLE);
        db.execSQL(categoryTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + table.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + categoryTable.TABLE_NAME);
        onCreate(db);
    }


    //  Insert Queries here
    public void insertCategories(Context context) {
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] categories = new String[]{"Food", "Bills", "Travel", "Shopping", "Entertainment"};
        ContentValues values = new ContentValues();
        for (int i = 0; i < categories.length; i++) {
            values.put(categoryTable.CATEGORY, categories[i]);
            long row = db.insert(categoryTable.TABLE_NAME, null, values);
            values.clear();
            Log.i("Categories row:", String.valueOf(row));
        }
    }

    public void insertNewCategory(Context context, String categoryName){
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(categoryTable.CATEGORY, categoryName);
        long row = db.insert(categoryTable.TABLE_NAME, null, values);
        values.clear();
        Log.d("New Category Added:", categoryName);
    }

    public void insertRecord(Context context, String amount, String category, String description) {
        MySqliteTaskHelper helper = MySqliteTaskHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysofweek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        ContentValues values = new ContentValues();
        values.put(table.DATE, String.valueOf(date + "/" + month + "/" + year));
        values.put(table.TIME, String.valueOf(hour + ":" + minutes));
        values.put(table.DAY_OF_WEEK, daysofweek[dayofWeek - 1]);
        values.put(table.CATEGORY, category);
        values.put(table.AMOUNT, amount);
        values.put(table.DESCRIPTION, description);
        long row = db.insert(table.TABLE_NAME, null, values);
        Log.i("SQL INSERT", String.valueOf(row));
    }

    //  Fetch data queries here
    public ArrayList<TransactionRecord> getRowsAsArrayListObjects(Context context) {
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        int row = cursor.getCount();
        ArrayList<TransactionRecord> arrayListItems = new ArrayList<TransactionRecord>();
        cursor.moveToFirst();
        for (int i = 0; i < row; i++) {
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String dayOfWeek = cursor.getString(3);
            String category = cursor.getString(4);
            String amount = cursor.getString(5);
            String description = cursor.getString(6);
            TransactionRecord item = new TransactionRecord(date, time, dayOfWeek, category, amount, description);
            arrayListItems.add(item);
            cursor.moveToNext();
        }
        return arrayListItems;
    }


    public ArrayList<TransactionRecord> getRecentTwoRows(Context context) {
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();

        // using this counter so our for loop only runs 2 times
        int counter = 0;

        Cursor cursor = db.query(table.TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
        int row = cursor.getCount();

        ArrayList<TransactionRecord> arrayListItems = new ArrayList<TransactionRecord>();
        cursor.moveToLast();

        for (int i = row; i > 0; i--) {
            String date = cursor.getString(1);
            String time = cursor.getString(2);
            String dayOfWeek = cursor.getString(3);
            String category = cursor.getString(4);
            String amount = cursor.getString(5);
            String description = cursor.getString(6);
            TransactionRecord item = new TransactionRecord(date, time, dayOfWeek, category, amount, description);
            arrayListItems.add(item);
            if (counter == 1) {
                break;
            }
            counter++;
            cursor.moveToPrevious();
        }
        return arrayListItems;
    }

    public float getExpensesForToday(Context context) {
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        float total = 0;
        String TodaysDate = getTodaysOrYesterdaysDate(0);
        Cursor cursor = db.query(table.TABLE_NAME, new String[]{table.AMOUNT}, table.DATE + "=?", new String[]{TodaysDate}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String amount = cursor.getString(0);
            total = total + Float.valueOf(amount);
            cursor.moveToNext();
        }
        return total;
    }


    public String getTodaysOrYesterdaysDate(int i) {
        String date;
        Calendar calendar = Calendar.getInstance();
        if(i==1){
            calendar.add(Calendar.DATE, -1);
        }
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        date = day + "/" + month + "/" + year;
        return date;
    }

    public ArrayList<String> getCategoriesList(Context context) {
        ArrayList<String> categories = new ArrayList<>();
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.query(categoryTable.TABLE_NAME, new String[]{categoryTable.CATEGORY}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return categories;
    }

    public ArrayList<String> getDates(Context context) {
        ArrayList<String> dates = new ArrayList<>();
        sqliteTaskHelperInstance = MySqliteTaskHelper.getInstance(context);
        db = sqliteTaskHelperInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + table.DATE + " FROM " + table.TABLE_NAME +" ORDER BY "+table.DATE+" DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dates.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return dates;
    }

    // Delete queries here
    public void removeCategory(Context context, String categoryName){
        MySqliteTaskHelper mySqliteTaskHelper = MySqliteTaskHelper.getInstance(context);
        db = mySqliteTaskHelper.getWritableDatabase();
        db.delete(categoryTable.TABLE_NAME, categoryTable.CATEGORY+"=?", new String[]{categoryName});
        db.close();
    }

}
